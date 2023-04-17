package main;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Benchmark
{
    private LocalDateTime start;
    private LocalDateTime end;
    private long time = 0;
    private boolean startCheck = false;
    private boolean endCheck = false;

public Benchmark()
{
   
}
   
    void startTimer()
    {
        startCheck = true;
        start = LocalDateTime.now();
    }
   
    void endTimer()
    {
        endCheck = true;
        end = LocalDateTime.now();
    }
   
    long getCurrentTime()
    {
    	return ChronoUnit.SECONDS.between(start, LocalDateTime.now());
    }
    
    long getResultTime()
    {
        if(startCheck && endCheck) {
            return ChronoUnit.MILLIS.between(start, end);
        } else {
            System.out.print("BenchMark Failed. ");
        }
        return time;
    }
   
}