package sample.managers;

import java.util.ArrayList;

public class NewVisionManager implements IManager{

    /**
     *
     * @param paths - paths to folders with jsons.
     */
    NewVisionManager(ArrayList<String> paths){

    }

    @Override
    public void start() {
        //стартуємо NewVision в який передаємо перший
        //запускаємо таймер в якому крутиться перевірка на роботу NewVision-а
        //якщо не робить то запускаємо наступну папку з json-ами
    }

    @Override
    public void stop() {

    }
}
