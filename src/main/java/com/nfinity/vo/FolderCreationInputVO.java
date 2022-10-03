package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class FolderCreationInputVO {
    @NotBlank
    @JsonProperty("folder_name")
    private String folderName;
    @NotEmpty
    private List<NftVO> records;
}
