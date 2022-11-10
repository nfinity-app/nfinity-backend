package com.nfinity.service.impl;

import com.nfinity.entity.*;
import com.nfinity.enums.ErrorCode;
import com.nfinity.enums.Status;
import com.nfinity.exception.BusinessException;
import com.nfinity.repository.*;
import com.nfinity.service.LoyaltyProgramService;
import com.nfinity.util.BeansUtil;
import com.nfinity.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoyaltyProgramServiceImpl implements LoyaltyProgramService {
    private final LoyaltyProgramRepository loyaltyProgramRepository;
    private final LoyaltyProgramCollectionRepository loyaltyProgramCollectionRepository;
    private final InstagramHashtagRepository instagramHashtagRepository;
    private final TierRepository tierRepository;
    private final TierUserRepository tierUserRepository;
    private final CollectionRepository collectionRepository;

    @Override
    @Transactional
    public Long saveLoyaltyProgram(LoyaltyProgramVO vo) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Long programId = saveLoyaltyProgramToDb(vo, timestamp, Status.DISABLE.getValue(), Status.DISABLE.getValue());
        vo.setId(programId);
        savePartDataToDb(vo, timestamp);

        return programId;
    }

    @Override
    public Long createLoyaltyProgram(LoyaltyProgramVO vo) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Long programId = saveLoyaltyProgramToDb(vo, timestamp, Status.DISABLE.getValue(), Status.ENABLE.getValue());
        vo.setId(programId);
        savePartDataToDb(vo, timestamp);

        return programId;
    }

    @Override
    public LoyaltyProgramCollectionsVO getLoyaltyProgram(Long userId) {
        //1. get data from table loyalty_program
        LoyaltyProgramCollectionsVO vo = new LoyaltyProgramCollectionsVO();
        Optional<LoyaltyProgramEntity> loyaltyProgramEntityOptional = loyaltyProgramRepository.findByUserId(userId);
        if(loyaltyProgramEntityOptional.isEmpty()) {
            return null;
        }

        LoyaltyProgramEntity loyaltyProgramEntity = loyaltyProgramEntityOptional.get();
        BeanUtils.copyProperties(loyaltyProgramEntity, vo, BeansUtil.getNullFields(loyaltyProgramEntity));
        Long programId = loyaltyProgramEntity.getId();

        //2. get data from table loyalty_program_collection
        List<LoyaltyProgramCollectionEntity> loyaltyProgramCollectionEntities = loyaltyProgramCollectionRepository.findAllByProgramId(programId);
        if(!CollectionUtils.isEmpty(loyaltyProgramCollectionEntities)) {
            List<CollectionRewardVO> collectionRewardVOList = new ArrayList<>(loyaltyProgramCollectionEntities.size());
            for (LoyaltyProgramCollectionEntity loyaltyProgramCollectionEntity : loyaltyProgramCollectionEntities) {
                CollectionRewardVO collectionRewardVO = new CollectionRewardVO();
                BeanUtils.copyProperties(loyaltyProgramCollectionEntity, collectionRewardVO, BeansUtil.getNullFields(loyaltyProgramCollectionEntity));

                Optional<CollectionEntity> collectionEntityOptional = collectionRepository.findById(loyaltyProgramCollectionEntity.getCollectionId());
                collectionEntityOptional.ifPresent(collectionEntity -> collectionRewardVO.setCollectionName(collectionEntity.getName()));

                //3. get data from table instagram_hashtag
                List<InstagramHashtagEntity> instagramHashtagEntities = instagramHashtagRepository.findAllByUsername(loyaltyProgramCollectionEntity.getInstagramUsername());
                if(!CollectionUtils.isEmpty(instagramHashtagEntities)){
                    List<InstagramHashtagVO> instagramHashtagVOList = new ArrayList<>(instagramHashtagEntities.size());
                    for(InstagramHashtagEntity instagramHashtagEntity : instagramHashtagEntities){
                        InstagramHashtagVO instagramHashtagVO = new InstagramHashtagVO();
                        BeanUtils.copyProperties(instagramHashtagEntity, instagramHashtagVO, BeansUtil.getNullFields(instagramHashtagEntity));
                        instagramHashtagVOList.add(instagramHashtagVO);
                    }
                    collectionRewardVO.setInstagramHashtags(instagramHashtagVOList);
                }
                collectionRewardVOList.add(collectionRewardVO);
            }
            vo.setCollectionRewards(collectionRewardVOList);
        }

        //4. get data from table tier
        List<TierEntity> tierEntities = tierRepository.findAllByProgramId(programId);
        if(!CollectionUtils.isEmpty(tierEntities)){
            List<TierVO> tierVOList = new ArrayList<>(tierEntities.size());
            for(TierEntity tierEntity : tierEntities){
                TierVO tierVO = new TierVO();
                BeanUtils.copyProperties(tierEntity, tierVO, BeansUtil.getNullFields(tierEntity));
                tierVOList.add(tierVO);
            }
            vo.setTiers(tierVOList);
        }

        return vo;
    }

    @Override
    public PageModel<TierUserVO> getTierMembers(Long userId) {
        PageModel<TierUserVO> pageModel = new PageModel<>();

        Optional<LoyaltyProgramEntity> loyaltyProgramEntityOptional = loyaltyProgramRepository.findByUserId(userId);
        if(loyaltyProgramEntityOptional.isEmpty()){
            throw new BusinessException(ErrorCode.LOYALTY_PROGRAM_NOT_FOUND);
        }
        Long programId = loyaltyProgramEntityOptional.get().getId();
        long total = tierRepository.countAllByProgramId(programId);
        List<TierEntity> tierEntities = tierRepository.findAllByProgramId(programId);

        List<TierUserVO> tierUserVOList = new ArrayList<>(tierEntities.size());
        if(!CollectionUtils.isEmpty(tierEntities)){
            for(TierEntity tierEntity : tierEntities){
                TierUserVO tierUserVO = new TierUserVO();
                Long tierId = tierEntity.getId();
                long members = tierUserRepository.countByTierId(tierId);
                tierUserVO.setName(tierEntity.getName());
                tierUserVO.setMembers(members);
                tierUserVOList.add(tierUserVO);
            }
        }
        pageModel.setTotal(total);
        pageModel.setRecords(tierUserVOList);
        return pageModel;
    }

    @Override
    @Transactional
    public Long deleteLoyaltyProgram(Long userId) {
        Optional<LoyaltyProgramEntity> loyaltyProgramEntityOptional = loyaltyProgramRepository.findByUserId(userId);
        if(loyaltyProgramEntityOptional.isEmpty()){
            throw new BusinessException(ErrorCode.LOYALTY_PROGRAM_NOT_FOUND);
        }

        Long programId = loyaltyProgramEntityOptional.get().getId();

        loyaltyProgramCollectionRepository.deleteAllByProgramId(programId);
        instagramHashtagRepository.deleteAllByProgramId(programId);
        tierRepository.deleteAllByProgramId(programId);
        loyaltyProgramRepository.deleteById(programId);

        return programId;
    }

    @Override
    public Long editLoyaltyProgram(LoyaltyProgramVO vo) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Long programId = saveLoyaltyProgramToDb(vo, timestamp, Status.ENABLE.getValue(), Status.ENABLE.getValue());
        vo.setId(programId);
        savePartDataToDb(vo, timestamp);

        return programId;
    }

    @Transactional
    Long saveLoyaltyProgramToDb(LoyaltyProgramVO vo, Timestamp timestamp, int queryStatus, int savedStatus){
        LoyaltyProgramEntity loyaltyProgramEntity;
        Optional<LoyaltyProgramEntity> loyaltyProgramEntityOptional = loyaltyProgramRepository.findByUserIdAndStatus(vo.getUserId(), queryStatus);
        if(loyaltyProgramEntityOptional.isPresent()){
            loyaltyProgramEntity = loyaltyProgramEntityOptional.get();
        }else if(Status.ENABLE.getValue() == queryStatus){
            throw new BusinessException(ErrorCode.LOYALTY_PROGRAM_NOT_FOUND);
        }else{
            loyaltyProgramEntity = new LoyaltyProgramEntity();
        }

        BeanUtils.copyProperties(vo, loyaltyProgramEntity, BeansUtil.getNullFields(vo));
        loyaltyProgramEntity.setStatus(savedStatus);
        loyaltyProgramEntity.setCreateTime(timestamp);
        loyaltyProgramEntity.setUpdateTime(timestamp);
        return loyaltyProgramRepository.save(loyaltyProgramEntity).getId();
    }

    @Transactional
    void savePartDataToDb(LoyaltyProgramVO vo, Timestamp timestamp){
        if(Objects.nonNull(vo.getCollectionId())) {
            LoyaltyProgramCollectionEntity loyaltyProgramCollectionEntity;
            Optional<LoyaltyProgramCollectionEntity> loyaltyProgramCollectionEntityOptional = loyaltyProgramCollectionRepository.findByProgramIdAndCollectionId(vo.getId(), vo.getCollectionId());
            loyaltyProgramCollectionEntity = loyaltyProgramCollectionEntityOptional.orElseGet(LoyaltyProgramCollectionEntity::new);

            BeanUtils.copyProperties(vo, loyaltyProgramCollectionEntity, BeansUtil.getNullFields(vo));
            loyaltyProgramCollectionEntity.setProgramId(vo.getId());
            loyaltyProgramCollectionEntity.setCreateTime(timestamp);
            loyaltyProgramCollectionEntity.setUpdateTime(timestamp);
            loyaltyProgramCollectionRepository.save(loyaltyProgramCollectionEntity);
        }

        if(Objects.nonNull(vo.getInstagramEngagement()) && Status.ENABLE.getValue() == vo.getInstagramEngagement() && !CollectionUtils.isEmpty(vo.getInstagramHashtags())) {
            String username = vo.getInstagramUsername();
            for(InstagramHashtagVO instagramHashtagVO : vo.getInstagramHashtags()){
                InstagramHashtagEntity instagramHashtagEntity;
                Optional<InstagramHashtagEntity> instagramHashtagEntityOptional = instagramHashtagRepository.findByIdAndUsername(instagramHashtagVO.getId(), username);
                instagramHashtagEntity = instagramHashtagEntityOptional.orElseGet(InstagramHashtagEntity::new);

                instagramHashtagEntity.setProgramId(vo.getId());
                instagramHashtagEntity.setName(instagramHashtagVO.getName());
                instagramHashtagEntity.setPerLikePoints(instagramHashtagVO.getPerLikePoints());
                instagramHashtagEntity.setUsername(username);
                instagramHashtagEntity.setCreateTime(timestamp);
                instagramHashtagEntity.setUpdateTime(timestamp);
                instagramHashtagRepository.save(instagramHashtagEntity);
            }
        }

        if(Objects.nonNull(vo.getTiersCreation()) && Status.ENABLE.getValue() == vo.getTiersCreation() && !CollectionUtils.isEmpty(vo.getTiers())){
            for(TierVO tierVO : vo.getTiers()){
                TierEntity tierEntity;
                Optional<TierEntity> tierEntityOptional = tierRepository.findByIdAndProgramId(tierVO.getId(), vo.getId());
                tierEntity = tierEntityOptional.orElseGet(TierEntity::new);

                tierEntity.setProgramId(vo.getId());
                tierEntity.setName(tierVO.getName());
                tierEntity.setRequiredPoints(tierVO.getRequiredPoints());
                tierEntity.setCreateTime(timestamp);
                tierEntity.setUpdateTime(timestamp);
                tierRepository.save(tierEntity);
            }
        }
    }
}
