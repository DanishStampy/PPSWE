package com.example.ppswe.view.vitalsign;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ppswe.R;
import com.example.ppswe.model.SingletonVitalSign;

import java.util.List;

public class VitalSign_ResultFragment extends Fragment {

    private TextView tvBP, tvBodyTemp, tvPulseRate, tvRespirationRate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vital_sign__result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Init singleton
        SingletonVitalSign singletonVitalSign = SingletonVitalSign.getInstance();

        tvBP = view.findViewById(R.id.tvResultBP);
        tvBodyTemp = view.findViewById(R.id.tvResultBodyTemp);
        tvPulseRate = view.findViewById(R.id.tvResultPulseRate);
        tvRespirationRate = view.findViewById(R.id.tvResultRespiratoryRate);

        tvBP.setText(BPresult(singletonVitalSign.getBP()));
        tvBodyTemp.setText(BodyTemperatureResult(singletonVitalSign.getBodyTemp()));
        tvPulseRate.setText(PulseRateResult(singletonVitalSign.getPulseRate()));
        tvRespirationRate.setText(RespirationResult(singletonVitalSign.getRespiraitonRate()));
    }

    private String PulseRateResult(double pulseRate) {

        String result = "";

        if(pulseRate > 60 && pulseRate <= 100) {
            result += "The pulse rate is normal. Healthy adults pulse rate ranges from 60 to 100 beats per minute (BPM). ";
        } else if (pulseRate > 100) {
            result += "The pulse rate is high. The pulse rate higher than 100 beats per minute (BPM). It is also called tachycardia. You may experiencing shortness of breath, dizziness or fainting spells.";
        } else if (pulseRate < 60) {
            result += "The pulse rate is low. The pulse rate lower than 60 beats per minute (BPM). It is also called bradycardia. Bradycardia can be life threatening if the heart is unable to maintain a rate that pumps enough oxygen-rich blood throughout the body.";
        }

        return result;
    }

    private String RespirationResult(double respiraitonRate) {

        String result = "";

        if(respiraitonRate > 12 && respiraitonRate <= 20) {
            result += "The respiratory rate is normal. The normal adult respiration rate at rest is between 12 to 20 breaths per minute. ";
        } else if (respiraitonRate > 20) {
            result += "The respiratory rate is abnormal. High respiration rate that more than 20 breaths per minute is considered have tachypnea. ";
        } else if (respiraitonRate < 12) {
            result += "The respiratory rate is abnormal. Low respiration rate that less than 12 breaths per minute is considered have bradypnea. ";
        }

        return result;
    }

    private String BodyTemperatureResult(double bodyTemp) {

        String result = "";

        if (bodyTemp >= 36.5 && bodyTemp <= 37.2) {
            result += "The body temperature is normal because your temperature in range between 36.5°C to 37.2°C";
        } else if (bodyTemp > 37.2) {
            result += "The body temperature is too high. You might have a fever. It is because your body temperature higher than 37.2°C";
        } else if (bodyTemp < 36.5) {
            result += "The body temperature is too low. You might have hypothermia. It is because your body temperature lower than 36.5°C";
        }

        return result;
    }

    private String BPresult(List<Double> BP) {

        String result = "";
        double systolic = BP.get(0);
        double diastolic = BP.get(1);

        if((systolic >= 90 && systolic < 120) && (diastolic >=60 && diastolic < 80)) {
            result += "The systolic and diastolic pressure is normal. Currently, you are free from high and low blood pressure. It is because your BP overall is between 120/80 and 90/60";
        } else if(systolic < 90 && diastolic < 60) {
            result += "The systolic and diastolic pressure is low. You may have a low blood pressure. It is because your BP overall is lower than 90/60";
        } else if(systolic >= 120 && diastolic <= 80) {
            result += "The systolic and diastolic pressure is high. You may have a high blood pressure or hypertension. It is because your BP overall is high than 120/80";
        }

        return result;
    }
}