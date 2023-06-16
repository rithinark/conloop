package com.mock.conloop.model;

import lombok.Data;

@Data
public class TokenResponse {
        private String refreshToken;
        private String jwtToken;

        public TokenResponse(String refreshToken, String jwtToken) {
                this.refreshToken = refreshToken;
                this.jwtToken = jwtToken;

        }

}
