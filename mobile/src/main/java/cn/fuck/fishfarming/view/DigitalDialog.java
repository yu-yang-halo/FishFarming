package cn.fuck.fishfarming.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.fuck.fishfarming.R;


/**
 * Created by Administrator on 2017/3/15.
 */

public class DigitalDialog extends Dialog {
    public DigitalDialog(Context context) {
        super(context);
    }
    public DigitalDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder implements AlwaysClickButton.LVMuiltClickCallBack{
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private LVCallback mCallback=null;

        private Button btnConfig,btnCancel;
        private ImageButton btnBack;
        private int m_value=0;
        private TextView edit_value;
        private Button btn_one;
        private Button btn_two;
        private Button btn_three;
        private Button btn_four;
        private Button btn_five;
        private Button btn_six;
        private Button btn_seven;
        private Button btn_eigth;
        private Button btn_nine;
        private Button btn_zero;
        private AlwaysClickButton btn_add;
        private AlwaysClickButton btn_reduce;
        private ImageButton btn_clear;
        private ImageButton btn_cancel;
        private boolean bFirstDigitClicked=true;
        private int Max = 99;
        private int Min = 0;
        private TextView tv_Max;
        private TextView tv_Min;
        private int ParMsg;
        private TextView tvMaxTitle;
        private TextView tvMinTitle;

        public Builder(Context context,LVCallback callback,int max, int min, int value ,int par) {
            this.context = context;
            mCallback = callback;

            Max=max;
            Min=min;
            m_value = value;
            ParMsg = par;
        }

        @Override
        public void onMuiltClick(int par,int isSend)
        {
            if (isSend == 1)
            {
                switch (par)
                {
                    case 0:
                        if (m_value<Max)
                        {
                            m_value++;
                            bFirstDigitClicked=false;
                            edit_value.setText( Integer.toString(m_value));
                        }
                        break;
                    case 1:
                        if (m_value>Min)
                        {
                            m_value--;
                            bFirstDigitClicked=false;
                            edit_value.setText( Integer.toString(m_value));
                        }
                        break;
                }
            }

        }

        public void UpdateValues()
        {
            edit_value.setText(Integer.toString(m_value));
            tv_Max.setText(Integer.toString(Max));
            tv_Min.setText(Integer.toString(Min));

        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }
        public interface LVCallback{
            public void onConfirmClick(int value, int par);
        }
        public DigitalDialog create() {
            WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(displayMetrics);

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final DigitalDialog dialog = new DigitalDialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View layout = inflater.inflate(R.layout.digital, null);

            // set the dialog title
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



            btnConfig = (Button)layout.findViewById(R.id.confirm);
            btn_cancel = (ImageButton)layout.findViewById(R.id.cancel);
            btn_one = (Button)layout.findViewById(R.id.one);
            btn_two = (Button)layout.findViewById(R.id.two);
            btn_three = (Button)layout.findViewById(R.id.three);
            btn_four = (Button)layout.findViewById(R.id.four);
            btn_five = (Button)layout.findViewById(R.id.five);
            btn_six = (Button)layout.findViewById(R.id.six);
            btn_seven = (Button)layout.findViewById(R.id.seven);
            btn_eigth = (Button)layout.findViewById(R.id.eight);
            btn_nine = (Button)layout.findViewById(R.id.nine);
            btn_zero = (Button)layout.findViewById(R.id.zero);
            btn_add = (AlwaysClickButton)layout.findViewById(R.id.add);
            btn_reduce = (AlwaysClickButton)layout.findViewById(R.id.reduce);
            edit_value = (TextView) layout.findViewById(R.id.value_digital);
            btn_clear = (ImageButton)layout.findViewById(R.id.clear);
            btnBack = (ImageButton)layout.findViewById(R.id.back);

            tv_Max=(TextView)layout.findViewById(R.id.tv_max);
            tv_Min = (TextView)layout.findViewById(R.id.tv_min);
            tvMaxTitle = (TextView)layout.findViewById(R.id.tv_max_title);
            tvMinTitle = (TextView)layout.findViewById(R.id.tv_min_title);
            UpdateValues();

            btn_add.setValve(0,this);
            btn_reduce.setValve(1,this);

            tvMaxTitle.setText("最大");
            tvMinTitle.setText("最小");
            btnConfig.setText("确定");
            SetListener();

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String stValue = edit_value.getText().toString();
                    bFirstDigitClicked=false;
                    if (stValue.length()>1)
                    {
                        String stTemp = stValue.substring(0,stValue.length()-1);
                        m_value = Integer.valueOf(stTemp);
                    }
                    else
                    {
                        m_value = 0;
                    }
                    edit_value.setText(Integer.toString(m_value));
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        dialog.cancel();
                }
            });

            btnConfig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (m_value<=Max && m_value>=Min)
                    {
                        if (mCallback != null)
                            mCallback.onConfirmClick(m_value,ParMsg);
                        dialog.cancel();
                    }
                    else if (m_value>Max)
                    {
                        Toast.makeText(context, "输入值大于最大值",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(context, "输入值小于最小值",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            dialog.setContentView(layout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            return dialog;
        }

        public void SetListener()
        {
            btn_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDigitalNum(1);
                }
            });
            btn_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDigitalNum(2);
                }
            });
            btn_three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDigitalNum(3);
                }
            });
            btn_four.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDigitalNum(4);
                }
            });
            btn_five.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDigitalNum(5);
                }
            });
            btn_six.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDigitalNum(6);
                }
            });
            btn_seven.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDigitalNum(7);
                }
            });
            btn_eigth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDigitalNum(8);
                }
            });
            btn_nine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDigitalNum(9);
                }
            });
            btn_zero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickDigitalNum(0);
                }
            });

            btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_value = 0;
                bFirstDigitClicked=true;
                edit_value.setText( Integer.toString(m_value));
            }
        });

        }

        public void clickDigitalNum(int num)
        {
            if (bFirstDigitClicked)
            {
                m_value = num;
                bFirstDigitClicked=false;
            }
            else
            {
                m_value = m_value*10+num;
            }

            edit_value.setText( Integer.toString(m_value));
        }



    }
}
