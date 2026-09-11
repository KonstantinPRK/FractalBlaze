package application.execution;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.IntConsumer;

@Component
public final class LineTaskExecutor {
    private final TaskRunner taskRunner;
    private final WorkRangePartitioner workRangePartitioner;

    public LineTaskExecutor(TaskRunner taskRunner, WorkRangePartitioner workRangePartitioner) {
        this.taskRunner = taskRunner;
        this.workRangePartitioner = workRangePartitioner;
    }

    public void executeLines(int lineCount, IntConsumer lineProcessor) {
        List<Callable<Void>> tasks = createLineRanges(lineCount).stream()
                .map(lineRange -> (Callable<Void>) () -> {
                    for (int lineIndex = lineRange.firstIndex(); lineIndex < lineRange.endIndex(); lineIndex++) lineProcessor.accept(lineIndex);
                    return null;
                })
                .toList();

        try (TaskBatch<Void> taskBatch = taskRunner.execute(tasks)) {
            while (taskBatch.hasNextResult()) taskBatch.takeNextResult();
        }
    }

    public <TaskResult> List<TaskResult> executeRanges(int lineCount, Function<WorkRange, TaskResult> rangeProcessor) {
        List<Callable<TaskResult>> tasks = createLineRanges(lineCount).stream()
                .map(lineRange -> (Callable<TaskResult>) () -> rangeProcessor.apply(lineRange))
                .toList();

        List<TaskResult> results = new ArrayList<>(tasks.size());

        try (TaskBatch<TaskResult> taskBatch = taskRunner.execute(tasks)) {
            while (taskBatch.hasNextResult()) results.add(taskBatch.takeNextResult());
        }

        return results;
    }

    private List<WorkRange> createLineRanges(int lineCount) {
        return workRangePartitioner.partition(lineCount, taskRunner.getThreadCount());
    }
}
