package com.trading212.judge.model.entity.submission;

import com.trading212.judge.model.entity.submission.enums.CodeLanguageEnum;
import com.trading212.judge.model.entity.base.BaseEntity;

import java.time.Instant;
import java.util.Objects;

public class CodeLanguageEntity extends BaseEntity {

    public static final String TABLE_NAME = "code_languages";

    private final CodeLanguageEnum name;

    public CodeLanguageEntity(Integer id, Instant createdAt, CodeLanguageEnum name) {
        super(id, createdAt);
        this.name = name;
    }

    public CodeLanguageEnum getLanguageEnum() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeLanguageEntity that = (CodeLanguageEntity) o;
        return name == that.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
