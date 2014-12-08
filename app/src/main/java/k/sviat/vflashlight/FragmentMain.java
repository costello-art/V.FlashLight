package k.sviat.vflashlight;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import k.sviat.vflashlight.fragment.Fragment1;
import k.sviat.vflashlight.fragment.Fragment2;

/**
 * Created by Sviat on 08.12.14.
 */
public class FragmentMain extends Activity {
    Fragment1 frag1;
    Fragment2 frag2;
    FragmentTransaction fTrans;
    CheckBox chbStack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        frag1 = new Fragment1();
        frag2 = new Fragment2();

        chbStack = (CheckBox) findViewById(R.id.chbStack);
    }

    public void onClick(View v) {
        fTrans = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.btnAdd:
                fTrans.add(R.id.fragmentContainer, frag1);
                break;
            case R.id.btnRemove:
                fTrans.remove(frag1);
                break;
            case R.id.btnReplace:
                fTrans.replace(R.id.fragmentContainer, frag2);
            default:
                break;
        }
        if (chbStack.isChecked()) fTrans.addToBackStack(null);
        fTrans.commit();
    }
}
