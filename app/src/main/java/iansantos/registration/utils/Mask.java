package iansantos.registration.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class Mask {
    public static String CPF_MASK = "###.###.###-##";

    private static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "");
    }

    private static boolean isASign(char c) {
        return c == '.' || c == '-';
    }

    public static TextWatcher insert(final String mask, final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = Mask.unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int index = 0;
                for (int i = 0; i < mask.length(); i++) {
                    char m = mask.charAt(i);
                    if (m != '#') {
                        if (index == str.length() && str.length() < old.length()) {
                            continue;
                        }
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(index);
                    } catch (Exception e) {
                        break;
                    }
                    index++;
                }
                if (mascara.length() > 0) {
                    char last_char = mascara.charAt(mascara.length() - 1);
                    boolean hadSign = false;
                    while (isASign(last_char) && str.length() == old.length()) {
                        mascara = mascara.substring(0, mascara.length() - 1);
                        last_char = mascara.charAt(mascara.length() - 1);
                        hadSign = true;
                    }
                    if (mascara.length() > 0 && hadSign) {
                        mascara = mascara.substring(0, mascara.length() - 1);
                    }
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }
}