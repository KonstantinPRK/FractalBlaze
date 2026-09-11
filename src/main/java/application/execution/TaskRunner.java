package application.execution;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class TaskRunner implements AutoCloseable {
    @FunctionalInterface
    public interface RangeTask<TaskResult> {
        TaskResult execute(int firstIndex, int endIndex);
    }

    private final int CONFIGURED_THREAD_COUNT;
    private final ExecutorService executor;
    private int activeThreadCount;

    public TaskRunner(int configuredThreadCount) {
        CONFIGURED_THREAD_COUNT = configuredThreadCount;
        activeThreadCount = configuredThreadCount;
        executor = Executors.newFixedThreadPool(configuredThreadCount);
    }

    public void selectMode(ExecutionMode executionMode) {
        activeThreadCount = executionMode == ExecutionMode.SINGLE_THREAD ? 1 : CONFIGURED_THREAD_COUNT;
    }

    public int getThreadCount() {
        return activeThreadCount;
    }

    public <TaskResult> TaskBatch<TaskResult> executeRanges(int itemCount, RangeTask<TaskResult> rangeTask) {
        TaskBatch<TaskResult> taskBatch = new TaskBatch<>(executor);

        try {
            int taskCount = Math.min(itemCount, activeThreadCount);

            for (int taskIndex = 0; taskIndex < taskCount; taskIndex++) {
                int firstIndex = (int) ((long) itemCount * taskIndex / taskCount);
                int endIndex = (int) ((long) itemCount * (taskIndex + 1) / taskCount);
                taskBatch.submit(() -> rangeTask.execute(firstIndex, endIndex));
            }

            return taskBatch;
        } catch (RuntimeException | Error exception) {
            taskBatch.close();
            throw exception;
        }
    }

    @Override
    public void close() {
        executor.close();
    }
}
