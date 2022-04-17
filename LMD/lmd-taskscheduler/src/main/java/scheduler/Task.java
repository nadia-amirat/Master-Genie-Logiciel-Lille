package scheduler;

import scheduler.Generators.Range;
import scheduler.Generators.Sequence;
import scheduler.Generators.TaskScheduler;
import scheduler.exceptions.*;
import scheduler.utils.Combinations;
import scheduler.utils.ValuesChecker;

import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;


public class Task {

    private ValuesChecker valuesChecker;
    public TaskScheduler months = null;
    public TaskScheduler hours = null;
    public TaskScheduler minutes = null;
    public TaskScheduler daysOfMonth = null;
    public TaskScheduler daysOfWeek = null;
    public String filePath = "/Home/Script.sh";


    public Task(TaskScheduler minutes, TaskScheduler hours, TaskScheduler daysOfMonth, TaskScheduler months, TaskScheduler daysOfWeek) {
        this.minutes = minutes;
        this.hours = hours;
        this.daysOfMonth = daysOfMonth;
        this.months = months;
        this.daysOfWeek = daysOfWeek;
    }

    public Task() {
        valuesChecker = new ValuesChecker();
    }

    public Task setMinutes(TaskScheduler minutes) throws NonAuthorizedMinuteException {
        valuesChecker.checkMinutes(minutes);
        this.minutes = minutes;
        return this;
    }

    public Task setMonths(TaskScheduler months) throws NonAuthorizedMonthException {
        valuesChecker.checkMonths(months);
        this.months = months;
        return this;
    }

    public Task setHours(TaskScheduler hours) throws NonAuthorizedHourException {
        valuesChecker.checkHours(hours);
        this.hours = hours;
        return this;
    }

    public Task setDaysOfMonth(TaskScheduler daysOfMonth) throws NonAuthorizedDayOfMonthException {
        valuesChecker.checkDaysOfMonth(daysOfMonth);
        this.daysOfMonth = daysOfMonth;
        return this;
    }

    public Task setDaysOfWeek(TaskScheduler daysOfWeek) throws NonAuthorizedDayOfWeekException {
        valuesChecker.checkDaysOfWeek(daysOfWeek);
        this.daysOfWeek = daysOfWeek;
        return this;
    }

    public String getStringValue(TaskScheduler taskScheduler) {
        if (taskScheduler == null)
            return "*";
        else
            return taskScheduler.toString();
    }

    public String toString() {
        String res = "";
        res = getStringValue(this.minutes) + " " + getStringValue(this.hours) + " " + getStringValue(this.daysOfMonth) + " " + getStringValue(this.months) + " " + getStringValue(this.daysOfWeek) + " " + this.filePath;
        return res;
    }


    public String displayTask(){
        //for minutes
        String displayMinutes="";
        if(this.minutes==null){
            displayMinutes="At every minute";
        }
        else if(this.minutes instanceof Range){
            displayMinutes="At every minute from "+((Range) this.minutes).getMin() + " through " + ((Range) this.minutes).getMax()+" with step "+((Range) this.minutes).getStep();
        }else if(this.minutes instanceof Sequence){
            displayMinutes="At ";
            int i;
            for(i=0;i< ((Sequence)this.minutes).values.size()-1;i++){
                displayMinutes+=((Sequence)this.minutes).values.get(i)+",";
            }
            displayMinutes+=" and "+((Sequence)this.minutes).values.get(i);
        }
        //for hours
        String displayHours="";
        if(this.hours==null){
            displayHours="";
        }
        else if(this.minutes instanceof Range){
            displayHours="past every hour from "+((Range) this.hours).getMin() + " through " + ((Range) this.hours).getMax()+" with step "+((Range) this.hours).getStep();;
        }else if(this.minutes instanceof Sequence){
            displayHours=" past hour ";
            int i;
            for(i=0;i< ((Sequence)this.hours).values.size()-1;i++){
                displayHours+=((Sequence)this.hours).values.get(i)+",";
            }
            displayHours+=" and "+((Sequence)this.hours).values.get(i);
        }

        //for days
        String displayDays="";
        if(this.daysOfMonth==null){
            displayDays="";
        }
        else if(this.daysOfMonth instanceof Range){
            displayDays="on every day-of-month from "+((Range) this.daysOfMonth).getMin() + " through " + ((Range) this.daysOfMonth).getMax()+" with step "+((Range) this.daysOfMonth).getStep();;
        }else if(this.daysOfMonth instanceof Sequence){
            displayDays=" on day-of-month ";
            int i;
            for(i=0;i< ((Sequence)this.daysOfMonth).values.size()-1;i++){
                displayDays+=((Sequence)this.daysOfMonth).values.get(i)+",";
            }
            displayDays+=" and "+((Sequence)this.daysOfMonth).values.get(i);
        }

        //for days
        String displayMonths="";
        if(this.months==null){
            displayMonths="";
        }
        else if(this.months instanceof Range){
           displayMonths="in every month from "+ Month.of(((Range) this.months).getMin()).name() + " through " + Month.of(((Range) this.months).getMax()).name()+" with step "+((Range) this.months).getStep();;
        }else if(this.months instanceof Sequence){
            displayMonths=" in ";
            int i;
            for(i=0;i< ((Sequence)this.months).values.size()-1;i++){
                displayMonths+=Month.of(((Sequence)this.months).values.get(i)).name()+",";
            }
            displayMonths+=" and "+Month.of(((Sequence)this.months).values.get(i)).name();
        }

        //return
        return displayMinutes+" "+displayHours+" "+displayDays+" "+displayMonths;
    }

    public Boolean exist(Date dateTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        int am = cal.get(Calendar.AM_PM);
        int _minutes = cal.get(Calendar.MINUTE);
        int _hour = cal.get(Calendar.HOUR_OF_DAY);
        int _day_of_month = cal.get(Calendar.DAY_OF_MONTH);
        int _day_of_week = cal.get(Calendar.DAY_OF_WEEK) -1;
        int _month = cal.get(Calendar.MONTH) + 1;


        if ((this.minutes == null || this.minutes.exist(_minutes))
                && (this.hours == null || this.hours.exist(_hour))
                && (this.months == null || this.months.exist(_month))
                && ((this.daysOfMonth == null || this.daysOfMonth.exist(_day_of_month))
                || (this.daysOfWeek == null || this.daysOfWeek.exist(_day_of_week))))
            return true;
        return false;
    }

    public int listNextDates() {
        System.out.println("The task will occur on each one of the following dates: ");
        @SuppressWarnings("unchecked")
        Vector<Integer>[] arr = new Vector[4];
        for (int i = 0; i < arr.length; i++)
            arr[i] = new Vector<Integer>();

        // Vector 1 (minutes)
        if (minutes == null) {
            for (int j = 0; j < 60; j++)
                arr[0].add(j);
        } else {
            for (int i : minutes.generateValues()) {

                arr[0].add(i);
            }
        }
        // Vector 2 (hours)
        if (hours == null) {
            for (int j = 0; j < 24; j++)
                arr[1].add(j);
        } else {
            for (int i : hours.generateValues()) {

                arr[1].add(i);
            }
        }
        // Vector 3 ( days of month)
        if (daysOfMonth == null) {
            for (int j = 1; j < 32; j++)
                arr[2].add(j);
        } else {
            for (int i : daysOfMonth.generateValues()) {
                arr[2].add(i);
            }
        }
        // Vector 4 (months)
        if (months == null) {
            for (int j = 1; j < 13; j++)
                arr[3].add(j);
        } else {
            for (int i : months.generateValues()) {
                arr[3].add(i);
            }
        }
        ArrayList<ArrayList<Integer>> possibleDates = Combinations.print(arr);
        HashMap<String, Date> dates = new HashMap<>();
        for (int i = 0; i < possibleDates.size(); i++) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            YearMonth yearMonthObject = YearMonth.of(year, possibleDates.get(i).get(3));
            int daysInMonth = yearMonthObject.lengthOfMonth();
            if (possibleDates.get(i).get(2) <= daysInMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.AM_PM, Calendar.AM);
                calendar.set(Calendar.MINUTE, possibleDates.get(i).get(0));
                calendar.set(Calendar.HOUR, possibleDates.get(i).get(1));
                calendar.set(Calendar.DAY_OF_MONTH, possibleDates.get(i).get(2));
                calendar.set(Calendar.MONTH, possibleDates.get(i).get(3)-1);
                Date d = calendar.getTime();
                dates.put(d.toString(), d);
            }
        }
        for (String k : dates.keySet())
            System.out.println(k);
        return dates.size();
    }

}
