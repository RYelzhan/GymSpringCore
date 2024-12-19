package com.epam.wca.common.gymcommon.auth_dto;

public record UserAuthenticatedDTO(
        String username,
        String password
) {
}
