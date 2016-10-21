package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends Activity implements View.OnClickListener {

    Button equals;  Button point;    Button C;         Button back;
    Button plus;    Button minus;    Button multiply; Button dev;
    Button one;     Button two;      Button three;
    Button four;    Button five;     Button six;
    Button seven;   Button eight;    Button night;
    Button zero;    Button change;   Button trans;
    Button p;//多项式
    String tran;    int tran_kind=0;//进制转换数10进制形式存储及进制转换类型
    SoundPool soundPool;
    HashMap clickId;
    TextView show;//下方显示框：显示输入的数据、运算结果或进制转换
    TextView text;//上方显示框——单步运算：用于保存的第一个数及运算符；多项式运算：用于保存算术式
    int boom_time=0;
    int boom_count=0;
    boolean polynomial=false;//判断运算模式，用于切换按键响应
    int sign[]; int num_sign;//多项式运算的算术符位置及个数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        tran=new String("");
        boom_count = 0;
        Random random=new Random();
        boom_time = random.nextInt(80);
        while (boom_time <= 40) {
            boom_time += random.nextInt(10);
        }
        //多项式运算符位初始化
        sign=new int[20];
            for(int i=0;i<10;i++) sign[i]=0;
        num_sign=0;
        //按键设置
        show=(TextView)findViewById(R.id.showView);
        text = (TextView) findViewById(R.id.textView);
        p=(Button) findViewById(R.id.P);
        point = (Button) findViewById(R.id.point);
        plus= (Button) findViewById(R.id.plus);
        minus= (Button) findViewById(R.id.minus);
        multiply=(Button)findViewById(R.id.multiply);
        dev=(Button)findViewById(R.id.dev);
        equals = (Button) findViewById(R.id.equals);
        zero= (Button) findViewById( R.id.zero);
        one= (Button) findViewById(R.id.one);
        two= (Button) findViewById(R.id.two);
        three= (Button) findViewById(R.id.three);
        four= (Button) findViewById(R.id.four);
        five= (Button) findViewById(R.id.five);
        six= (Button) findViewById(R.id.six);
        seven= (Button) findViewById(R.id.seven);
        eight= (Button) findViewById(R.id.eight);
        night= (Button) findViewById(R.id.night);
        C=(Button) findViewById(R.id.C);
        change=(Button)findViewById(R.id.change);
        back=(Button)findViewById(R.id.back);
        trans= (Button) findViewById(R.id.trans);
        //显示面板初始化
        show.setText(new String("0"));     text.setText(new String(""));
        //按键声音ID设置
        soundPool=new SoundPool(12,AudioManager.STREAM_MUSIC,1);
        clickId = new HashMap<Integer, Integer>();
        clickId.put(0,soundPool.load(this, R.raw.click,1));
        clickId.put(1,soundPool.load(this, R.raw.aha1,1));
        clickId.put(2,soundPool.load(this, R.raw.aha2,1));
        clickId.put(3,soundPool.load(this, R.raw.aha3,1));
        clickId.put(4,soundPool.load(this, R.raw.aha4,1));
        clickId.put(5,soundPool.load(this, R.raw.aha5,1));
        //添加监听事件
        p.setOnClickListener(this);
        equals.setOnClickListener(this);   point.setOnClickListener(this);
        plus.setOnClickListener(this);     minus.setOnClickListener(this);
        multiply.setOnClickListener(this); dev.setOnClickListener(this);
        zero.setOnClickListener(this); change.setOnClickListener(this);
        one.setOnClickListener(this);  two.setOnClickListener(this);; three.setOnClickListener(this);
        four.setOnClickListener(this); five.setOnClickListener(this); six.setOnClickListener(this);
        seven.setOnClickListener(this);eight.setOnClickListener(this);night.setOnClickListener(this);
        C.setOnClickListener(this);     back.setOnClickListener(this);        trans.setOnClickListener(this);
    }
    //总监听事件
    public void onClick(View v) {
        //如果更改运算方式，清空显示
        if(v.getId()==R.id.P){
            show.setText("");
            text.setText("");
            //更改运算方式
            polynomial=!polynomial;
            if(((Button)v).getText().equals("多项式×")) {
                num_sign=0;
                ((Button) v).setText("多项式√");
            }
            else {
                num_sign=0;
                ((Button) v).setText("多项式×");
            }
            return;
        }
        //如果为多项式模式，则进入多项式的监听事件
        if(polynomial) {
            Polynomial(v);
            return;
        }
        //显示字符大小设置
        if(show.getText().length()>14)
            show.setTextSize(20);
        else
            show.setTextSize(30);
        if(text.getText().length()>10)
            text.setTextSize(20);
        else
            text.setTextSize(30);
        //按键音播放
        Random random=new Random();
        int ran=0;
        if(random.nextInt(10)>11){
            ran=random.nextInt(4);
            switch (ran){
                case 0:soundPool.play((Integer) clickId.get(1),1,1, 0, 0, 1);break;
                case 1:soundPool.play((Integer) clickId.get(2),1,1, 0, 0, 1);break;
                case 2:soundPool.play((Integer) clickId.get(3),1,1, 0, 0, 1);break;
                case 3:soundPool.play((Integer) clickId.get(4),1,1, 0, 0, 1);break;
                case 4:soundPool.play((Integer) clickId.get(5),1,1, 0, 0, 1);break;
            }
        }
        else
            soundPool.play((Integer) clickId.get(0),1,1, 0, 0, 1);
        String text_str=text.getText().toString();
        //如果在进制转换后要进行其他操纵，将进制转换后的数据还原为十进制
        if(v.getId()!=R.id.trans) {
            if (tran.length() > 0) {
                if (negative)
                    show.setText("-" + tran);
                else
                    show.setText(tran);
            }
            tran = new String("");
            negative=false;
        }
        String show_str=show.getText().toString();
        //如果是等于号，获取结果显示在下方的显示屏
        if (v.getId() == R.id.equals) {
            Equals(v);
        }
        else {
            switch (v.getId()) {
                case R.id.trans:Change();break;//进制转换
                case R.id.back://回退
                    boom_count++;
                    if(show_str.length()>0)
                        show.setText(show_str.substring(0,show_str.length()-1));
                    break;
                case R.id.change://改变正负
                    boom_count++;
                    if(show_str.length()==0){
                        show.setText(new String("-0"));
                    }
                    else if(show_str.indexOf("-")==0){
                        show.setText(show_str.substring(1,show_str.length()));
                    }
                    else {
                        show_str=new String("-"+show_str);
                        show.setText(show_str);
                    }
                    break;
                case R.id.C://清屏
                    text.setText(new String(""));
                    show.setText(new String(""));
                    break;
                case R.id.plus:
                case R.id.minus:
                case R.id.multiply:
                case R.id.dev:
                    //运算符操作
                    boom_count++;
                    if(text_str.length()==0){//未输入运算符，添加并将数和运算符放入上方显示屏
                        if(show_str.length()==0||(show_str.length()==1&&show_str.equals("-")))
                            show_str=new String("0");
                        else if(show_str.indexOf(".")==show.length()-1)
                            show_str=new String(show_str.substring(0,show_str.length()-1));
                        text_str=new String(show_str+((Button)v).getText().toString());
                        show_str=new String("");
                        show.setText(show_str);
                        text.setText(text_str);
                    }
                    else if(show_str.length()==0||(show_str.length()==1&&show_str.equals("-"))){
                        //如果已输入运算符且下方屏幕无值，改变运算符
                       text_str=new String(text_str.substring(0,text_str.length()-1)+((Button)v).getText().toString());
                       text.setText(text_str);
                    }
                    else if(text_str.length()!=0)//如果已经有运算符，且下方屏幕有值，获取结果
                        Equals(v);
                    break;
                case R.id.point://小数点添加以及取消
                    boom_count++;
                    if(show_str.length()==0){
                        show.setText(new String("0."));
                    }
                    else if(!show_str.contains(".")&&(show_str.length()==1&&show_str.equals("-"))){
                        show.setText(show_str+"0.");
                    }
                    else if(!show_str.contains(".")){
                        show.setText(show_str+".");
                    }
                    else if(show_str.contains(".")&&(show_str.length()-1==show_str.indexOf("."))){
                        show.setText(show_str.substring(0,show_str.length()-1));
                    }
                    break;
                case R.id.zero:
                case R.id.one:   case R.id.two:   case R.id.three:
                case R.id.four:  case R.id.five:  case R.id.six:
                case R.id.seven: case R.id.eight:case R.id.night:
                    //数字添加
                    boom_count++;
                    if(show_str.equals("0")||show_str.length()==0||show_str.charAt(0)=='I'||show_str.contains("E")){
                        show.setText(((Button)v).getText().toString());
                    }
                    else if(show_str.equals("-0")){
                        show.setText("-"+((Button)v).getText().toString());
                    }
                    else {
                        show.setText(show_str+((Button)v).getText().toString());
                    }
                    break;
            }
        }
        /*
        if (boom_count >= boom_time) {
            boom_count = 0;
            boom_time = random.nextInt(100);
            while (boom_time <= 40) {
                boom_time += random.nextInt(10);
            }
            Intent intent = new Intent(MainActivity.this, Halo.class);
            startActivity(intent);
        }*/
    }
    //获取单项式运算结果
    private void Equals(View v){
        String s =show.getText().toString();
        String result=new String("");
        if(s.length()==0||s.charAt(0)=='I'||(s.length()==1&&s.equals("-")))
            s = "0";
        else if(s.length()!=0&&s.indexOf(".")==s.length()-1)
            s=s.substring(0,s.indexOf("."));
        String t=text.getText().toString();
        String sign;
        if(t.length()==0) {
            t ="0";
            sign ="+";
        }
        else{
            t=text.getText().toString().substring(0,text.getText().toString().length()-1);
            sign=text.getText().toString().substring(text.getText().toString().length()-1,text.getText().toString().length());
        }
        double d1=Double.parseDouble(t);
        double d2=Double.parseDouble(s);
        double d =Double.parseDouble("0");
        switch (sign) {
            case "+":
                d=d1+d2;
                break;
            case "-":
                d=d1-d2;
                break;
            case "×":
                if(d2==0) d=0;
                else
                    d=d1*d2;
                break;
            case "÷":
                if (d2 != 0) {
                    d=d1/d2;
                }
                break;
        }
        result=new String(d+"");
        //将浮点型结果进行精确
        if(sign.equals("+")||sign.equals("-")){
            int p1_length=0;int p2_length=0;
            if(t.contains(".")) p1_length=t.length()-1-t.indexOf(".");
            if(s.contains(".")) p2_length=s.length()-1-s.indexOf(".");
            if(p1_length>0||p2_length>0) {
                int delete=0;
                int max = p1_length;
                if (p2_length > max) max = p2_length;
                if (result.length() - 1 - result.indexOf(".")>max){
                    result=result.substring(0,result.indexOf(".")+2+max);
                    char up=result.charAt(result.length()-1);
                    if(up=='9') {
                        up=result.charAt(result.length()-2);
                        up++;
                        while (up==':'){
                            delete++;
                            up=result.charAt(result.length()-2-delete);
                            if(up=='.') {
                                delete++;
                                up = result.charAt(result.length() - 2 - delete);
                            }
                            up++;
                        }
                        result=result.substring(0,result.length()-2-delete)+up;
                    }
                    else if(result.charAt(result.length()-1)=='0')
                        result=result.substring(0,result.length()-1);
                }
            }
        }
        //将乘法后的浮点型结果精确
        if(sign.equals("×")){
            result=Normal(t,s,result);
        }
        //根据最后输入的是否为等于号进行不同的数据显示设置
        if(v.getId()==R.id.equals){
            show.setText(result.toString());
            text.setText(new String(""));
        }
        else {
            text.setText(result + ((Button) v).getText().toString());
            show.setText(new String(""));
        }
    }
    //多项式运算的监听事件
    private void Polynomial(View v){
        if(show.getText().length()>14)
            show.setTextSize(20);
        else
            show.setTextSize(30);
        soundPool.play((Integer) clickId.get(0),1,1, 0, 0, 1);
        if(text.getText().toString().length()>0&&v.getId()!=R.id.back) {
            num_sign=0;
            text.setText("");
        }
        String show_str=show.getText().toString();
            switch (v.getId()) {
                case R.id.back://回退
                    //回退输入上一次输入
                    if(show_str.length()>0&&text.getText().length()==0) {
                        //删除运算符。更改运算符信息
                        if(show_str.length()-1==sign[num_sign]&&num_sign>0){
                            sign[num_sign]=0;
                            num_sign--;
                        }
                        show.setText(show_str.substring(0, show_str.length() - 1));
                    }
                    //已运算获得结果，回退之前的表达式
                    else if(text.getText().toString().length()>0){
                        show.setText(text.getText().toString().substring(0,text.getText().toString().length()-1));
                        text.setText("");
                    }
                    break;
                case R.id.change://对最后一个数据的正负进行更改
                    if(show_str.length()==0){
                        show.setText(new String("-0"));
                    }
                    else if(num_sign>0&&show_str.length()-1==sign[num_sign]){
                        show.append("-0");
                    }
                    else if(num_sign==0&&!show_str.contains("-")){
                        show.setText("-"+show_str);
                    }
                    else if(num_sign>0 && show_str.lastIndexOf("-")>sign[num_sign]){
                        show.setText(show_str.substring(0,show_str.lastIndexOf("-"))+show_str.substring(show_str.lastIndexOf("-")+1,show_str.length()));
                    }
                    else if(num_sign>0 && show_str.lastIndexOf("-")<=sign[num_sign]){
                        show.setText(show_str.substring(0,sign[num_sign]+1)+"-"+show_str.substring(sign[num_sign]+1,show_str.length()));
                    }
                    else if(num_sign==0&&show_str.lastIndexOf("-")==0){
                        show.setText(show_str.substring(1,show_str.length()));
                    }
                    break;
                case R.id.C://清屏
                    show.setText("");
                    num_sign=0;
                    break;
                case R.id.plus:
                case R.id.minus:
                case R.id.multiply:
                case R.id.dev:
                        if(show_str.length()==0);
                        else if(num_sign==0&&show_str.charAt(show_str.length()-1)!='.'&&show_str.charAt(show_str.length()-1)!='-'){
                            show.append(((Button)v).getText());
                            num_sign++;
                            sign[num_sign]=show_str.length();
                        }
                        else if(num_sign>0&&show_str.length()-1==sign[num_sign]){
                            show.setText(show_str.substring(0,show_str.length()-1)+((Button)v).getText());
                        }
                        else if(show_str.length()-1>sign[num_sign]){
                            if(show_str.charAt(show_str.length()-1)!='-'&&show_str.charAt(show_str.length()-1)!='.'){
                                show.append(((Button)v).getText());
                                num_sign++;
                                sign[num_sign]=show_str.length();
                            }
                        }
                    break;
                case R.id.point://对最后一个整数进行小数点添加以及删除
                    if(show_str.length()==0){
                        show.setText("0.");
                    }
                    else if(num_sign>0 && show_str.length()-1==sign[num_sign]){
                        show.append("0.");
                    }
                    else if(show_str.lastIndexOf("-")==show_str.length()-1&&show_str.length()-1!=sign[num_sign]){
                        show.append("0.");
                    }
                    else if(show_str.lastIndexOf(".")<sign[num_sign]){
                        show.append(".");
                    }
                    else if(show_str.lastIndexOf(".")==show_str.length()-1){
                        show.setText(show_str.substring(0,show_str.length()-1));
                    }
                    break;
                case R.id.zero:
                case R.id.one:   case R.id.two:   case R.id.three:
                case R.id.four:  case R.id.five:  case R.id.six:
                case R.id.seven: case R.id.eight: case R.id.night:
                    //数字添加
                    if((show_str.lastIndexOf("0")==0&&show_str.length()==1)||show_str.length()==0||show_str.charAt(0)=='I'){
                        show.setText(((Button)v).getText().toString());
                    }
                    else if(num_sign>0&&show_str.lastIndexOf("0")==show_str.length()-1&&show_str.length()-2==sign[num_sign]){
                        show.setText(show_str.substring(0,show_str.length()-1)+((Button)v).getText().toString());
                    }
                    else if(show_str.contains("-")&&show_str.lastIndexOf("-0")==show_str.length()-2){
                        show.setText(show_str.substring(0,show_str.length()-1)+((Button)v).getText().toString());
                    }
                    else {
                        show.append(((Button)v).getText().toString());
                    }
                    break;
                case R.id.equals://如果最后一位不为小数点、负号以及操作符，获得结果
                    if(num_sign>0&&show_str.length()-1>sign[num_sign]){
                        char a=show_str.charAt(show_str.length()-1);
                        if(a!='.'&&a!='-'){
                            Polynomial_Equals();
                        }
                    }
                    break;
            }
        if(text.getText().toString().length()>15) text.setTextSize(20);
    }
    //多项式运算符结果计算，直接正常表达式硬解
    private void Polynomial_Equals(){
        String show_str=show.getText().toString();
        Stack<Double> save_double=new Stack<>();//数字存储栈
        Stack<Double> temp_double=new Stack<>();//运算符存储栈
        Stack<String> save_string=new Stack<>();//数字临时存储栈
        Stack<String> temp_string=new Stack<>();//运算符临时存储栈
        //算式入栈
        save_double.push(Double.parseDouble(show_str.substring(0,sign[1])));
        sign[num_sign+1]=show_str.length();
        for(int i=1;i<=num_sign;i++){
            save_string.push(show_str.substring(sign[i],sign[i]+1));
                save_double.push(Double.parseDouble(show_str.substring(sign[i]+1,sign[i+1])));
        }
        String t_str;Double t_dou;Double temp;
        while(!save_string.empty()){
            //获取后操作数及操作符
            t_dou=save_double.pop();
            t_str=save_string.pop();
            if(t_str.equals("+")||t_str.equals("-")){
                while(t_str.equals("-")){//连减处理
                    if(save_string.empty()){
                        t_dou = save_double.pop() - t_dou;
                        save_double.push(t_dou);
                        break;
                    }
                    else if(save_string.peek().equals("-")){
                        t_dou = save_double.pop() + t_dou;
                        save_double.push(t_dou);
                    }
                    else
                        break;
                    t_dou=save_double.pop();
                    t_str=save_string.pop();
                }
                //加减之前有乘除处理
                if(!save_string.empty()||t_str.equals("+")) {
                    temp_double.push(t_dou);
                    temp_string.push(t_str);
                    while (!save_string.empty() && (save_string.peek().equals("×") || save_string.peek().equals("÷"))) {
                        t_dou = save_double.pop();
                        t_str = save_string.pop();
                        if (t_str.equals("×")) {//乘法处理
                            temp_double.push(t_dou);
                            temp_string.push(t_str);//乘法前有除号处理
                            while (!save_string.empty() && save_string.peek().equals("÷")) {
                                t_dou = save_double.pop();
                                save_string.pop();
                                //连除处理
                                if (save_string.empty() || !save_string.peek().equals("÷"))
                                    t_dou = save_double.peek() / t_dou;
                                else if (save_string.peek().equals("÷")) {
                                    temp = t_dou;
                                    t_dou = save_double.peek() * t_dou;
                                    t_dou = Double.parseDouble(Normal(save_double.peek().toString(), temp.toString(), t_dou.toString()));
                                }
                                save_double.pop();
                                save_double.push(t_dou);
                            }
                            t_dou = temp_double.pop();
                            temp_string.pop();
                            temp = t_dou;
                            t_dou = save_double.peek() * t_dou;
                            t_dou = Double.parseDouble(Normal(save_double.peek().toString(), temp.toString(), t_dou.toString()));
                        } else {//除法处理
                            if (save_string.empty() || !save_string.peek().equals("÷"))
                                t_dou = save_double.peek() / t_dou;
                            else
                                t_dou = save_double.peek() * t_dou;
                        }
                        save_double.pop();
                        save_double.push(t_dou);
                    }
                    t_dou = temp_double.pop();
                    t_str = temp_string.pop();
                   // double s=t_dou;double t=save_double.peek();
                    //加减号处理
                        if (t_str.equals("+")) {
                            if(!save_string.empty()) {//前面有运算符，为减则减，为加则加，
                                if (save_string.peek().equals("+"))
                                    t_dou = save_double.peek() + t_dou;
                                else
                                    t_dou = save_double.peek() - t_dou;
                            }
                            else//无则加
                                t_dou = save_double.peek() + t_dou;
                        }
                        else{
                            if(!save_string.empty()){//前面有运算符，为加则减，为减则加
                                if(save_string.peek().equals("-"))
                                    t_dou = save_double.peek() + t_dou;
                                else
                                    t_dou = save_double.peek() - t_dou;
                            }
                            else//无则减
                                t_dou = save_double.peek() - t_dou;
                        }
                        //浮点数加减法精确处理（“…000…001”成功，“…999…999”失败）
                       /* String result=new String(t_dou+"");
                        if(sign.equals("+")||sign.equals("-")){
                            int p1_length=0;int p2_length=0;
                            if(((Double)t).toString().contains(".")) p1_length=((Double)t).toString().length()-1-((Double)t).toString().indexOf(".");
                            if(((Double)s).toString().contains(".")) p2_length=((Double)s).toString().length()-1-((Double)s).toString().indexOf(".");
                            if(p1_length>0||p2_length>0) {
                                int delete=0;
                                int max = p1_length;
                                if (p2_length > max) max = p2_length;
                                if (result.length() - 1 - result.indexOf(".")>max){
                                   result=result.substring(0,result.indexOf(".")+2+max);
                                   char up=result.charAt(result.length()-1);
                                   if(up=='9') {
                                      up=result.charAt(result.length()-2);
                                      up++;
                                      while (up==':'){
                                        delete++;
                                        up=result.charAt(result.length()-2-delete);
                                        if(up=='.') {
                                            delete++;
                                            up = result.charAt(result.length() - 2 - delete);
                                        }
                                        up++;
                                      }
                                    result=result.substring(0,result.length()-2-delete)+up;
                                   }
                                   else if(result.charAt(result.length()-1)=='0')
                                        result=result.substring(0,result.length()-1);
                                    if(save_string.empty()){
                                        save_double.pop();
                                        text.setText(show_str.substring(0,show_str.length())+"=");
                                        show.setText(result);
                                        return;
                                    }
                                }
                            }
                        }*/
                }
            }
            else {
                if(t_str.equals("×")) {//乘法处理
                    temp_double.push(t_dou);
                    temp_string.push(t_str);
                    while (!save_string.empty()&&save_string.peek().equals("÷")){//前面有除号，处理
                        t_dou=save_double.pop();
                        save_string.pop();
                        if(save_string.empty()||!save_string.peek().equals("÷"))
                            t_dou=save_double.peek()/t_dou;
                        else if(save_string.peek().equals("÷")) {
                            temp = t_dou;
                            t_dou = save_double.peek() * t_dou;
                            t_dou = Double.parseDouble(Normal(save_double.peek().toString(), temp.toString(), t_dou.toString()));
                        }
                        save_double.pop();
                        save_double.push(t_dou);
                    }
                    t_dou=temp_double.pop();
                    temp_string.pop();
                    temp = t_dou;
                    t_dou = save_double.peek() * t_dou;
                    t_dou = Double.parseDouble(Normal(save_double.peek().toString(), temp.toString(), t_dou.toString()));
                }
                else{//除号处理
                    if(save_string.empty()||!save_string.peek().equals("÷"))
                        t_dou = save_double.peek() / t_dou;
                    else {
                        temp = t_dou;
                        t_dou = save_double.peek() * t_dou;
                        t_dou = Double.parseDouble(Normal(save_double.peek().toString(), temp.toString(), t_dou.toString()));
                    }
                }
            }
            save_double.pop();
            save_double.push(t_dou);
        }
        text.setText(show_str+"=");
        show.setText(save_double.peek().toString());
    }
    //浮点数乘法精确化
    String Normal(String t,String s,String result){
        int delete=0;
        if(true){
            int a=t.indexOf(".");
            int b=s.indexOf(".");
            int a_length=0,b_length=0;
            if(a>0) a_length=t.length()-1-t.indexOf(".");
            if(b>0) b_length=s.length()-1-s.indexOf(".");
            //非科学计数法且乘数与被乘数含有小数运算结果小数位数大于乘数和被乘数的小数位数之和
            if((a>0||b>0) && result.indexOf(".")>0 &&!result.contains("E")
                    && (result.length()-1-result.indexOf("."))!=a_length+b_length
                    &&result.length()>=result.indexOf(".") + a_length + b_length + 2) {
                //截取结果保留一位多余位
                result=result.substring(0, result.indexOf(".") + a_length + b_length + 2);
                char up=result.charAt(result.length()-1);
                if(up>='5'&&up<='9'){//多余位的值大于5
                    //获取前一个值，进位
                    up=result.charAt(result.length()-2);
                    up++;
                    while (up==':'){//如果为9，进位且截断
                        delete++;
                        up=result.charAt(result.length()-2-delete);
                        up++;
                    }
                    result=result.substring(0,result.length()-2-delete)+up;
                }
                else
                    result=result.substring(0,result.length()-1);
                delete=0;
            }
            else if((a<0 && b<0) && result.indexOf(".")>0&&!result.contains("E"))
                result=result.substring(0,result.indexOf("."));
        }
        if(result.indexOf(".")>0&&!result.contains("E")) {
            //如果非科学计数法且含有小数点
            //截断多余的0
            while (result.charAt(result.length() - 1 - delete) == '0')
                delete++;
            if (result.charAt(result.length() - 1 - delete) == '.')
                delete++;
            result = result.substring(0, result.length() - delete);
        }
        return new String(result);
    }
    boolean negative;
    //进制修改
    private void Change(){
        if(show.getText().toString().contains("E")&&!show.getText().toString().contains("(")){
            tran=new String(show.getText().toString());
            show.setText("I can't converts Scientific mode");
            return;
        }
        if(show.getText().toString().contains("I")){
            show.setText("Infinity");
            return;
        }
        if(tran.equals("")) {
            tran=new String(show.getText().toString());
            tran_kind=0;
            if(tran.length()==0||(tran.length()==1&&tran.contains("-")))
                tran=new String("0");
            else if(tran.indexOf(".")==tran.length()-1)
                tran=new String(tran.substring(0,tran.indexOf(".")));
            if(tran.contains("-")) {
                tran=new String(tran.substring(1,tran.length()));
                negative = true;
            }
            else
                negative=false;
        }
        tran_kind++;
        int num=1;
        switch (tran_kind){
            case 1:num=2;break;
            case 2:num=8;break;
            case 3:num=16;break;
            case 4:num=10;break;
            case 5:num=2;tran_kind=1;break;
        }
        if(num==10){
            if(negative)
                show.setText("-"+tran+"(10)");
            else
                show.setText(tran+"(10)");
            return;
        }
        String tran_show=new String("");
        int inter;double decimal;
        int flag; char big='A';
        if((tran.contains("."))) {
            inter = Integer.parseInt(tran.substring(0, tran.indexOf(".")));
            decimal=Double.parseDouble("0"+tran.substring(tran.indexOf("."),tran.length()));
        }
        else{
            inter=Integer.parseInt(tran);
            decimal=0.0;
        }
        do{
            flag=inter%num;
            inter=inter/num;
            if(flag<10)
              tran_show=new String(flag+""+tran_show);
            else{
                big=(char)(flag+55);
                tran_show=new String(big+tran_show);
            }
        }while(inter!=0);
        if(decimal>Math.pow(num,-12)){
            tran_show=new String(tran_show+".");
            double temp=decimal;int i=0;
            while (true) {
                i++;
                decimal = decimal * num;
                if (decimal < 10) {
                    tran_show = new String(tran_show + "" + (int) decimal);
                } else {
                    big = (char) ((int) decimal + 55);
                    tran_show = new String(tran_show+big);
                }
                decimal -= (int) decimal;
                if(i==12||decimal<Math.pow(num,-12)) break;
            }
        }
        if(tran_show.length()==0)
            tran_show=new String("0");
        tran_show=new String(tran_show+"("+num+")");
        if(negative) {
            tran_show = new String("-" + tran_show);
        }
        show.setText(tran_show);
        if(show.getText().length()>15)
            show.setTextSize(20);
    }
}
