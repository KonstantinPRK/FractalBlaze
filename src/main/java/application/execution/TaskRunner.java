package application.execution;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class TaskRunner implements AutoCloseable {
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

    public <TaskResult> TaskBatch<TaskResult> execute(List<? extends Callable<TaskResult>> tasks) {
        TaskBatch<TaskResult> taskBatch = new TaskBatch<>(executor);

        try {
            for (Callable<TaskResult> task : tasks) taskBatch.submit(task);
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
