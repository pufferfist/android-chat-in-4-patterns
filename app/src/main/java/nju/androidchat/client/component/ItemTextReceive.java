package nju.androidchat.client.component;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.StyleableRes;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.UUID;

import nju.androidchat.client.R;

public class ItemTextReceive extends LinearLayout {


    @StyleableRes
    int index0 = 0;

    private TextView textView;
    private Context context;
    private UUID messageId;
    private OnRecallMessageRequested onRecallMessageRequested;


    public ItemTextReceive(Context context, String text, UUID messageId) {
        super(context);
        this.context = context;
        inflate(context, R.layout.item_text_receive, this);
        this.textView = findViewById(R.id.chat_item_content_text);
        this.messageId = messageId;
        setText(text);
    }

    public void init(Context context) {

    }

    public String getText() {
        return textView.getText().toString();
    }

    public void setText(String text) {
        if (text.matches("^!\\[.*\\]\\(.*\\)$")) {
            String url;
            if (text.indexOf('(')+1==text.lastIndexOf(')')){
                url="http://img4.tuwandata.com/v3/thumb/jpg/MTA4NiwxMDI0LDAsOSwzLDEsLTEsTk9ORSwsLDkw/" +
                        "u/GLDM9lMIBglnFv7YKftLBuKr1GUNToe9JObNiKmO1LjEf2kSuxdQZgVpDKtFvzjRzPpiphVlHcbxSGFPs8l" +
                        "CuKaHNUZUpcmIStMxpwggpkb9.jpg";
            }else {
                url = text.substring(text.indexOf('(') + 1, text.lastIndexOf(')'));
            }
            String tag="<img src=\"" + url + "\">";
            textView.setText(Html.fromHtml(tag, Html.FROM_HTML_MODE_COMPACT, new NetImageGetter(), null));
        }
        else
            textView.setText(text);
    }

    class MyDrawableWrapper extends BitmapDrawable {
        private Drawable drawable;
        MyDrawableWrapper() {
        }
        @Override
        public void draw(Canvas canvas) {
            if (drawable != null)
                drawable.draw(canvas);
        }
        public Drawable getDrawable() {
            return drawable;
        }
        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }
    }

    class NetImageGetter implements Html.ImageGetter {

        @Override
        public Drawable getDrawable(String source) {
            MyDrawableWrapper myDrawable = new MyDrawableWrapper();
            Drawable drawable = context.getResources().getDrawable(R.mipmap.ic_launcher);
            drawable.setBounds(
                    0,
                    0,
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            myDrawable.setDrawable(drawable);
            Glide.with(ItemTextReceive.this)
                    .asBitmap()
                    .load(source)
                    .into(new BitmapTarget(myDrawable));
            return myDrawable;
        }
    }

    class BitmapTarget extends SimpleTarget<Bitmap> {
        private final MyDrawableWrapper myDrawable;
        BitmapTarget(MyDrawableWrapper myDrawable) {
            this.myDrawable = myDrawable;
        }

        @Override
        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
            Drawable drawable = new BitmapDrawable(context.getResources(), resource);
            //获取原图大小
            int width=drawable.getIntrinsicWidth() ;
            int height=drawable.getIntrinsicHeight();
            //自定义drawable的高宽, 缩放图片大小最好用matrix变化，可以保证图片不失真
            drawable.setBounds(0, 0, width, height);
            myDrawable.setBounds(0, 0, width, height);
            myDrawable.setDrawable(drawable);
            textView.setText(textView.getText());
            textView.invalidate();
        }
    }
}


