package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.strategy;

import org.springframework.web.multipart.MultipartFile;

public interface PersonImportStrategy {
    Long imports(MultipartFile multipartFile);
}
