package scheduler;

import scheduler.Generators.Range;
import scheduler.Generators.Sequence;
import scheduler.exceptions.*;
import scheduler.utils.Combinations;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        try {
            TaskBuilder taskBuilder = new TaskBuilder();
            Task task = taskBuilder.build()
            .setMinutes(new Range(1, 40, 10)) // 0-59
            .setHours(new Range(1, 3, 1)) // 0-23
            .setDaysOfMonth(new Sequence(Arrays.asList(1,5,7))) // 1-31
            .setMonths(new Range(1, 5, 1)) // 1-12
            .setDaysOfWeek(new Range(1, 7, 1)); // 0-7
            //System.out.println("displa " +task.displayTask());
            // 1/01/2021 00:00
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.AM_PM, Calendar.AM);
            calendar.set(Calendar.MONTH, 0); //0 --> JANVIER
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 59);
            Date d = calendar.getTime();

            System.out.println(task.displayTask());
            System.out.println("there is a task planned in this date: " + task.exist(d));

            task.listNextDates();
        } catch (NonAuthorizedMinuteException e) {
            System.out.println("Unsupported value for minutes");
            System.exit(1);
        } catch (NonAuthorizedHourException e) {
            System.out.println("Unsupported value for hours");
            System.exit(1);
        } catch (NonAuthorizedMonthException e) {
            System.out.println("Unsupported value for month");
            System.exit(1);
        } catch (NonAuthorizedDayOfWeekException e) {
            System.out.println("Unsupported value for day of week");
            System.exit(1);
        } catch (NonAuthorizedDayOfMonthException e) {
            System.out.println("Unsupported value for day of month");
            System.exit(1);
        }

    }

}
