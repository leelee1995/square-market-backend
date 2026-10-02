package com.lee.squaremarketbackend.dto.request;

import com.lee.squaremarketbackend.entity.ItemCondition;
import com.lee.squaremarketbackend.entity.WantedPostItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WantedPostItemPatchRequest {
    private String title;
    private WantedPostItemStatus status;
    private ItemCondition condition;
}