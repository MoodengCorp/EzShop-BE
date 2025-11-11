package com.moodeng.ezshop.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReissueResponseDto {
    private String access_token;

    @Builder.Default
    private String token_type = "Bearer";

    public static ReissueResponseDto of(String accessToken) {
        return ReissueResponseDto.builder()
                .access_token(accessToken)
                .build();
    }
}
