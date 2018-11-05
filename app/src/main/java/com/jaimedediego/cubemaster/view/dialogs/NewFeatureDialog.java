package com.jaimedediego.cubemaster.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.methods.PrefsMethods;

public class NewFeatureDialog extends Dialog {

    private Context context;

    private boolean didSomething = false;

    public NewFeatureDialog(@NonNull Context context) {
        super(context);
        this.context = context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_areyousure);

        RelativeLayout nameContainer = findViewById(R.id.puzzle_name_container);
        nameContainer.setVisibility(View.GONE);

        final TextView text = findViewById(R.id.info_areyousure);
        final Button ok = findViewById(R.id.accept);
        final Button cancel = findViewById(R.id.cancel);

        text.setText(R.string.new_feature_wca_scrambling);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefsMethods.getInstance().setScramble(true);
                PrefsMethods.getInstance().showedNewFeature(true);
                didSomething = true;
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefsMethods.getInstance().showedNewFeature(true);
                dismiss();
            }
        });
    }

    public boolean didSomething() {
        return didSomething;
    }
}
