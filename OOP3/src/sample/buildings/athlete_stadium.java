package sample.buildings;

import sample.RusName;

@RusName(r_name = "Легкоатлетический стадион")
public class athlete_stadium extends sport_fac {

    @RusName(r_name = "Сектор трека")
    private track track_sector;

    @RusName(r_name = "Вираж с ямой для воды")
    private Boolean pit;

    public Boolean getpit() {
        return this.pit;
    }

    public void setpit(Boolean pit) {
        this.pit = pit;
    }

    public track gettrack_sector() {
        return track_sector;
    }


    public void settrack_sector(track track_sector) {
        this.track_sector = track_sector;
    }


    @Override
    public void deleteObject() {
        settrack_sector(null);
    }


}
