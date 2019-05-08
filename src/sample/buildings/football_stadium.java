package sample.buildings;

import sample.RusName;

@RusName(r_name = "Футбольный стадион")
public class football_stadium extends stadium {

    @RusName(r_name = "Команда")
    private String team;

    @RusName(r_name = "Поле")
    private field pl_field;

    @RusName(r_name = "Ворота")
    private gate pl_gate;

    public String getteam() {
        return team;
    }

    public void setteam(String team) {
        this.team = team;
    }

    public field getpl_field() {
        return pl_field;
    }

    public void setpl_field(field pl_field) {
        this.pl_field = pl_field;
    }

    public gate getpl_gate() {
        return pl_gate;
    }

    public void setpl_gate(gate pl_gate) {
        this.pl_gate = pl_gate;
    }

    @Override
    public void deleteObject() {
        setpl_gate(null);
        setpl_field(null);
    }
}
