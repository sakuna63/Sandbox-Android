package com.sakuna63.sandbox;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.sakuna63.sandbox.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.buttonReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = (String) ((RadioButton) findViewById(binding.groupName.getCheckedRadioButtonId())).getText();
                Frag item = Frag.valueOf(name);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container_fragment, item.createFragment(), item.name());
                if (binding.checkboxBackStack.isChecked()) {
                    ft.addToBackStack(item.name());
                }
                ft.commit();
            }
        });

        binding.buttonPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStack();
            }
        });

        binding.buttonPopWithName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = (String) ((RadioButton) findViewById(binding.groupName.getCheckedRadioButtonId())).getText();
                Frag item = Frag.valueOf(name);
                getSupportFragmentManager().popBackStack(item.name(), 0);
            }
        });

        binding.buttonPopWithNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
    }

    enum Frag {
        A {
            @Override
            Fragment createFragment() {
                return new AFragment();
            }
        },
        B {
            @Override
            Fragment createFragment() {
                return new BFragment();
            }
        },
        C {
            @Override
            Fragment createFragment() {
                return new CFragment();
            }
        },
        D {
            @Override
            Fragment createFragment() {
                return new DFragment();
            }
        },
        E {
            @Override
            Fragment createFragment() {
                return new EFragment();
            }
        };

        abstract Fragment createFragment();
    }
}
