package sample.ParametersReader;

/**
 * Created by Andrew on 02.08.2017.
 */
public interface ISubtractorParameters {

    int getMatchingThreshold();

    void setMatchingThreshold(int matchingThreshold);

    int getRequiredMatches();

    void setRequiredMatches(int requiredMatches);

    int getUpdateFactor();

    void setUpdateFactor(int updateFactor);

}
