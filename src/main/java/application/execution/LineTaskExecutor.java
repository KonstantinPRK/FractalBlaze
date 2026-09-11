package application.execution;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

@Component
public final class LineTaskExecutor {
    private final TaskRunner taskRunner;

    public LineTaskExecutor(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    public void executeLines(int lineCount, IntConsumer lineProcessor) {
        try (TaskBatch<Void> taskBatch = taskRunner.executeRanges(lineCount, (firstLine, endLine) -> {
            for (int lineIndex = firstLine; lineIndex < endLine; lineIndex++) lineProcessor.accept(lineIndex);
            return null;
        })) {
            while (taskBatch.hasNextResult()) taskBatch.takeNextResult();
        }
    }

    public <TaskResult> List<TaskResult> executeRanges(int lineCount, TaskRunner.RangeTask<TaskResult> rangeTask) {
        List<TaskResult> results = new ArrayList<>(Math.min(lineCount, taskRunner.getThreadCount()));

        try (TaskBatch<TaskResult> taskBatch = taskRunner.executeRanges(lineCount, rangeTask)) {
            while (taskBatch.hasNextResult()) results.add(taskBatch.takeNextResult());
        }

        return results;
    }
}
