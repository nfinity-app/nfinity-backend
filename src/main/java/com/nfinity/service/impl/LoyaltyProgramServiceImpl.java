package com.nfinity.service.impl;

import com.nfinity.entity.InstagramHashtagEntity;
import com.nfinity.entity.LoyaltyProgramCollectionEntity;
import com.nfinity.entity.LoyaltyProgramEntity;
import com.nfinity.entity.TierEntity;
import com.nfinity.enums.Status;
import com.nfinity.repository.InstagramHashtagRepository;
import com.nfinity.repository.LoyaltyProgramCollectionRepository;
import com.nfinity.repository.LoyaltyProgramRepository;
import com.nfinity.repository.TierRepository;
import com.nfinity.service.LoyaltyProgramService;
import com.nfinity.util.BeansUtil;
import com.nfinity.vo.InstagramHashtagVO;
import com.nfinity.vo.LoyaltyProgramVO;
import com.nfinity.vo.TierVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoyaltyProgramServiceImpl implements LoyaltyProgramService {
    private final LoyaltyProgramRepository loyaltyProgramRepository;
    private final LoyaltyProgramCollectionRepository loyaltyProgramCollectionRepository;
    private final InstagramHashtagRepository instagramHashtagRepository;
    private final TierRepository tierRepository;

    @Override
    @Transactional
    public Long saveLoyaltyProgram(LoyaltyProgramVO vo, Long userId) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        LoyaltyProgramEntity loyaltyProgramEntity;
        Optional<LoyaltyProgramEntity> loyaltyProgramEntityOptional = loyaltyProgramRepository.findByUserId(userId);
        loyaltyProgramEntity = loyaltyProgramEntityOptional.orElseGet(LoyaltyProgramEntity::new);

        BeanUtils.copyProperties(vo, loyaltyProgramEntity, BeansUtil.getNullFields(vo));
        loyaltyProgramEntity.setCreateTime(timestamp);
        loyaltyProgramEntity.setUpdateTime(timestamp);
        Long programId = loyaltyProgramRepository.save(loyaltyProgramEntity).getId();

        if(Objects.nonNull(vo.getCollectionId())) {
            LoyaltyProgramCollectionEntity loyaltyProgramCollectionEntity;
            Optional<LoyaltyProgramCollectionEntity> loyaltyProgramCollectionEntityOptional = loyaltyProgramCollectionRepository.findFirstByProgramId(programId);
            loyaltyProgramCollectionEntity = loyaltyProgramCollectionEntityOptional.orElseGet(LoyaltyProgramCollectionEntity::new);

            BeanUtils.copyProperties(vo, loyaltyProgramCollectionEntity, BeansUtil.getNullFields(vo));
            loyaltyProgramCollectionEntity.setCreateTime(timestamp);
            loyaltyProgramCollectionEntity.setUpdateTime(timestamp);
            loyaltyProgramCollectionRepository.save(loyaltyProgramCollectionEntity);
        }

        if(Status.ENABLE.getValue() == vo.getInstagramEngagement() && !CollectionUtils.isEmpty(vo.getInstagramHashtags())) {
            String username = vo.getInstagramUsername();
            for(InstagramHashtagVO instagramHashtagVO : vo.getInstagramHashtags()){
                InstagramHashtagEntity instagramHashtagEntity;
                Optional<InstagramHashtagEntity> instagramHashtagEntityOptional = instagramHashtagRepository.findByIdAndUsername(instagramHashtagVO.getId(), username);
                instagramHashtagEntity = instagramHashtagEntityOptional.orElseGet(InstagramHashtagEntity::new);

                instagramHashtagEntity.setName(instagramHashtagVO.getName());
                instagramHashtagEntity.setPerLikePoints(instagramHashtagVO.getPerLikePoints());
                instagramHashtagEntity.setUsername(username);
                instagramHashtagEntity.setCreateTime(timestamp);
                instagramHashtagEntity.setUpdateTime(timestamp);
                instagramHashtagRepository.save(instagramHashtagEntity);
            }
        }

        if(!CollectionUtils.isEmpty(vo.getTiers())){
            for(TierVO tierVO : vo.getTiers()){
                TierEntity tierEntity;
                Optional<TierEntity> tierEntityOptional = tierRepository.findByIdAndProgramId(tierVO.getId(), programId);
                tierEntity = tierEntityOptional.orElseGet(TierEntity::new);

                tierEntity.setProgramId(programId);
                tierEntity.setName(tierVO.getName());
                tierEntity.setRequiredPoints(tierVO.getRequiredPoints());
                tierEntity.setCreateTime(timestamp);
                tierEntity.setUpdateTime(timestamp);
                tierRepository.save(tierEntity);
            }
        }

        return programId;
    }

    @Override
    public Long createLoyaltyProgram(LoyaltyProgramVO vo, Long userId) {
        return null;
    }

    @Override
    public LoyaltyProgramVO getLoyaltyProgram(Long userId) {
        return null;
    }

    @Override
    public Long editLoyaltyProgram(LoyaltyProgramVO vo, Long userId) {
        return null;
    }
}
