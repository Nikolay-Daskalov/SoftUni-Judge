package com.trading212.judge.service.submission;

import com.trading212.judge.model.dto.submission.SubmissionDTO;
import com.trading212.judge.model.entity.submission.SubmissionEntity;
import com.trading212.judge.repository.submission.SubmissionRepository;
import com.trading212.judge.service.enums.CodeResultEnum;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

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

    public boolean deleteAllByTasksId(Set<Integer> tasksIds) {
        return submissionRepository.deleteAllByTasksIds(tasksIds);
    }
}
