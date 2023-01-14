package com.trading212.judge.service.task;

import com.trading212.judge.model.dto.task.SubmissionDTO;
import com.trading212.judge.model.entity.task.SubmissionEntity;
import com.trading212.judge.repository.task.SubmissionRepository;
import com.trading212.judge.service.enums.CodeResultEnum;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public Integer save(Integer codeLanguageId, CodeResultEnum codeResult, Integer userId, Integer taskId, String executionTime) {
        return submissionRepository.save(codeLanguageId, codeResult, userId, taskId, executionTime);
    }

    public Optional<SubmissionDTO> findById(Integer id) {
        Optional<SubmissionEntity> submissionEntity = submissionRepository.findById(id);

        return mapToDTO(submissionEntity);
    }

    private Optional<SubmissionDTO> mapToDTO(Optional<SubmissionEntity> submissionEntity) {
        if (submissionEntity.isEmpty()) {
            return Optional.empty();
        }

        SubmissionEntity submission = submissionEntity.get();

        SubmissionDTO submissionDTO = new SubmissionDTO(
                submission.getId(),
                submission.getTaskId(),
                submission.getUserId(),
                submission.getResult(),
                submission.getCodeLanguageId(),
                submission.getExecutionTime(),
                submission.getCreatedAt()
        );

        return Optional.of(submissionDTO);
    }
}
