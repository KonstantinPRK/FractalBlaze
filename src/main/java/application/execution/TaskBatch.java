package application.execution;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public final class TaskBatch<TaskResult> implements AutoCloseable {
    private final BlockingQueue<Future<TaskResult>> completedTasks = new LinkedBlockingQueue<>();
    private final CompletionService<TaskResult> completionService;
    private final Set<Future<TaskResult>> pendingTasks = new HashSet<>();

    TaskBatch(ExecutorService executor) {
        completionService = new ExecutorCompletionService<>(executor, completedTasks);
    }

    void submit(Callable<TaskResult> task) {
        pendingTasks.add(completionService.submit(task));
    }

    public boolean hasNextResult() {
        return !pendingTasks.isEmpty();
    }

    public TaskResult takeNextResult() {
        try {
            Future<TaskResult> completedTask = completedTasks.take();
            pendingTasks.remove(completedTask);
            return completedTask.get();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Ожидание результата задачи прервано", exception);
        } catch (ExecutionException exception) {
            throw new IllegalStateException("Не удалось выполнить задачу", exception.getCause());
        }
    }

    @Override
    public void close() {
        pendingTasks.forEach(pendingTask -> pendingTask.cancel(true));
        pendingTasks.clear();
    }
}
