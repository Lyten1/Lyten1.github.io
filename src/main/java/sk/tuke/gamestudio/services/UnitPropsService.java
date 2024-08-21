package sk.tuke.gamestudio.services;

import sk.tuke.gamestudio.repos.UnitPropsRepo;
import sk.tuke.gamestudio.game.core.units.Unit;
import sk.tuke.gamestudio.game.core.units.UnitBuilder;
import sk.tuke.gamestudio.game.core.units.UnitClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitPropsService {

    @Autowired
    private UnitPropsRepo unitPropsRepo;

    public UnitBuilder fillDataToUnitByUClass(UnitClass unitClass){
        String name = null;
        switch (unitClass){
            case PIKEMAN -> name = "Pikeman";
            case ARCHER -> name = "Archer";
            case GRIFFIN -> name = "Griffin";
            case MONK -> name = "Monk";
            case SWORDSMAN -> name = "Swordsman";
            case CAVALIER -> name = "Cavalier";
            case ANGEL -> name = "Angel";
        }
        Unit unit = unitPropsRepo.getUnitByName(name);
        return new UnitBuilder().setPropertiesFromUnit(unit).setuClass(unitClass);
    }

    public List<Unit> getAllUnitsList(){
        return unitPropsRepo.getAll();
    }

    public int getUnitCostById(int id){
        return unitPropsRepo.getUnitCostById(id);
    }
}
