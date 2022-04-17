package scheduler.utils;

import scheduler.Generators.TaskScheduler;
import scheduler.exceptions.*;

import java.util.List;

public class ValuesChecker {

    public void checkMonths(TaskScheduler task) throws NonAuthorizedMonthException {
        List<Integer> months = task.generateValues();
        for (int m : months) {
            if (m < 1 || m > 12)
                throw new NonAuthorizedMonthException();
        }
    }

    public void checkDaysOfMonth(TaskScheduler task) throws NonAuthorizedDayOfMonthException {
        List<Integer> days = task.generateValues();
        for (int d : days) {
            if (d < 1 || d > 31) // 0 or 7 is sunday
                throw new NonAuthorizedDayOfMonthException();
        }
    }

    public void checkDaysOfWeek(TaskScheduler task) throws NonAuthorizedDayOfWeekException {
        List<Integer> days = task.generateValues();
        for (int d : days) {
            if (d < 0 || d > 8) // 0 or 7 is sunday
                throw new NonAuthorizedDayOfWeekException();
        }
    }

    public void checkHours(TaskScheduler task) throws NonAuthorizedHourException {
        List<Integer> hours = task.generateValues();
        for (int h : hours) {
            if (h < 0 || h > 23)
                throw new NonAuthorizedHourException();
        }
    }

    public void checkMinutes(TaskScheduler task) throws NonAuthorizedMinuteException {
        List<Integer> minutes = task.generateValues();
        for (int m : minutes) {
            if (m < 0 || m > 59)
                throw new NonAuthorizedMinuteException();
        }
    }
}
