package com.team5.on_stage.theme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ThemeDTO {
    private Long linkId;

    private String backgroundImage;

    private String buttonColor;

    private String profileColor;

    private String fontColor;

    private String iconColor;

    private int borderRadius;

}
