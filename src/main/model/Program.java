package main.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Program {
    private DayOfWeek zi;
    private LocalTime oraStart;
    private LocalTime oraFinal;

    public Program(DayOfWeek zi, LocalTime oraStart, LocalTime oraFinal) {
        this.zi = zi;
        this.oraStart = oraStart;
        this.oraFinal = oraFinal;
    }

    public boolean seSuprapune(Program altul){
        if( this.zi != altul.zi) return false;
        return this.oraStart.isBefore(altul.oraFinal) && altul.oraStart.isBefore(this.oraFinal);
    }

    public DayOfWeek getZi() { return zi; }
    public LocalTime getOraStart() { return oraStart; }
    public LocalTime getOraFinal() { return oraFinal; }

    @Override
    public String toString() { return zi + " " + oraStart + "-" + oraFinal; }
}
