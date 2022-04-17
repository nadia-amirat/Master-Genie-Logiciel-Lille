import org.junit.Test;
import scheduler.Generators.Range;
import scheduler.Generators.Sequence;
import scheduler.Task;
import scheduler.TaskBuilder;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class TaskSchedulerTest {

    @Test
    public void checkListTasksUsingRanges() {
        System.out.println("TEST 1:");
        TaskBuilder taskBuilder = new TaskBuilder();
        try {
            Task task = taskBuilder.build()
                    .setMinutes(new Range(1, 3, 1)) // 1,2,3
                    .setHours(new Range(0, 0, 1)) // 0
                    .setDaysOfMonth(new Range(1, 1, 1)) //1
                    .setMonths(new Range(1, 1, 1));// 2
            assertEquals(task.listNextDates(), 3);
            System.out.println(task);
            System.out.println(task.displayTask());
            System.out.println("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkListTasksUsingSequences() {
        System.out.println("TEST 2:");
        TaskBuilder taskBuilder = new TaskBuilder();
        try {
            Task task = taskBuilder.build()
                    .setMinutes(new Sequence(Arrays.asList(1, 10, 13))) // 1,10,13
                    .setHours(new Sequence(Arrays.asList(1, 10))) // 1,10
                    .setDaysOfMonth(new Range(1, 1, 1)) //1
                    .setMonths(new Range(1, 1, 1));// 2
            assertEquals(task.listNextDates(), 6);
            System.out.println(task);
            System.out.println(task.displayTask());
            System.out.println("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkStepsWorks() {
        System.out.println("TEST 3:");
        TaskBuilder taskBuilder = new TaskBuilder();
        try {
            Task task = taskBuilder.build()
                    .setMinutes(new Range(1, 60, 10)) // 1,11,21,31,41,51
                    .setHours(new Range(0, 0, 1)) // 0
                    .setDaysOfMonth(new Range(1, 1, 1)) //1
                    .setMonths(new Range(1, 1, 1));// 2
            assertEquals(task.listNextDates(), 6);
            System.out.println(task);
            System.out.println(task.displayTask());
            System.out.println("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkExistsWorks() {
        TaskBuilder taskBuilder = new TaskBuilder();
        try {
            Task task = taskBuilder.build()
                    .setMinutes(new Range(1, 60, 10))
                    .setHours(new Range(12, 23, 10))
                    .setDaysOfMonth(new Range(1, 21, 1))
                    .setMonths(new Range(3, 3, 1));

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.AM_PM, Calendar.AM);
            calendar.set(Calendar.MONTH, 2); // 2 c'est mars
            calendar.set(Calendar.DAY_OF_MONTH, 11);
            calendar.set(Calendar.HOUR, 22);
            calendar.set(Calendar.MINUTE, 21);
            Date d = calendar.getTime();
            assertTrue(task.exist(d));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkExistsWorksWhenSpecifyingDayOfWeek() {
        TaskBuilder taskBuilder = new TaskBuilder();
        try {
            Task task = taskBuilder.build().setDaysOfMonth(new Sequence(Arrays.asList(20)))
                    .setDaysOfWeek(new Sequence(Arrays.asList(1, 3))); //Monday, Wednesday

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.AM_PM, Calendar.AM);
            calendar.set(Calendar.MONTH, 10);
            calendar.set(Calendar.DAY_OF_MONTH, 17);
            calendar.set(Calendar.HOUR, 22);
            calendar.set(Calendar.MINUTE, 21);
            Date aWednesday = calendar.getTime(); // Wednesday 17th november
            assertTrue(task.exist(aWednesday));

            calendar.set(Calendar.DAY_OF_MONTH, 15);
            Date aMonday = calendar.getTime(); // Monday 15th november
            assertTrue(task.exist(aMonday));

            calendar.set(Calendar.DAY_OF_MONTH, 16);
            Date aTuesday = calendar.getTime(); // Tuesday 16th november
            assertFalse(task.exist(aTuesday));

            calendar.set(Calendar.DAY_OF_MONTH, 20);
            Date november20th = calendar.getTime(); // 20th November
            assertTrue(task.exist(november20th));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
