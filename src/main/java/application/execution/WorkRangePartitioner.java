package application.execution;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public final class WorkRangePartitioner {
    public List<WorkRange> partition(int itemCount, int requestedTaskCount) {
        if (itemCount == 0)return List.of();

        int taskCount = Math.min(itemCount, requestedTaskCount);
        List<WorkRange> workRanges = new ArrayList<>(taskCount);

        for (int taskIndex = 0; taskIndex < taskCount; taskIndex++) {
            int firstIndex = (int) ((long) itemCount * taskIndex / taskCount);
            int endIndex = (int) ((long) itemCount * (taskIndex + 1) / taskCount);
            workRanges.add(new WorkRange(firstIndex, endIndex));
        }

        return List.copyOf(workRanges);
    }
}
