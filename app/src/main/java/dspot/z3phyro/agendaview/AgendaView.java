package dspot.z3phyro.agendaview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Maykel on 4/6/2017.
 */

public class AgendaView extends View {
    protected final int TIME_LABEL_FONT_SIZE = (int) getResources().getDimension(R.dimen.origin_15dp);
    protected final int TIME_LABEL_VERTICAL_PADDING = (int) getResources().getDimension(R.dimen.origin_15dp)/2;

    protected final int DEPARTMENT_FONT_SIZE = (int) getResources().getDimension(R.dimen.origin_50dp)/2;
    protected final int DEPARTMENT_VERTICAL_PADDING = (int) getResources().getDimension(R.dimen.origin_30dp)/2;

    protected final int ROUND_RECT_PADDING = (int) getResources().getDimension(R.dimen.origin_20dp)/2;
    protected final int ROUND_RECT_HEIGHT = (int) getResources().getDimension(R.dimen.origin_120dp)/2;
    protected final int ROUND_RECT_RADIUS = (int) getResources().getDimension(R.dimen.origin_8dp)/2;

    protected final int ROUND_TEXT_SIZE = (int) getResources().getDimension(R.dimen.origin_30dp)/2;
    protected final int ROUND_TEXT_HORIZONTAL_PADDING = (int) getResources().getDimension(R.dimen.origin_20dp)/2;
    protected final int ROUND_HORIZONTAL_PADDING = (int) getResources().getDimension(R.dimen.origin_4dp)/2;

    protected final int VIEW_HEIGHT = (int) getResources().getDimension(R.dimen.origin_200dp)*5/2; // TODO Got to see this later

    protected final DateTimeUtility dateTimeUtility = DateTimeUtility.getInstance();

    protected int py = 0;

    protected int totalHours = 18;

    protected int initialHour = 6;

    protected int rulerStartX = (int) getResources().getDimension(R.dimen.origin_100dp)*2; // TODO Got to see this later

    protected int timeVisibility = View.VISIBLE;

    protected int viewWidth = (int) getResources().getDimension(R.dimen.origin_200dp)*10/2; // TODO Got to see this later

    protected int[] labelXPosition = new int[totalHours];

    protected HashMap<String, List<IShift>> ShiftHashData;

    protected List<ShiftRect> ShiftRects;

    protected OnShiftClickListener shiftClickListener;

    public AgendaView(Context context) {
        super(context);

        setupData();
    }

    public AgendaView(Context context, AttributeSet ats, int defaultStyle) {
        super(context, ats, defaultStyle);

    }

    public AgendaView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupData();
    }

    public void setTotalHours(int totalHours){
        this.totalHours = totalHours;
        labelXPosition = new int[totalHours];
    }

    public void setInitialHour(int initialHour){
        this.initialHour = initialHour;
    }

    public void setRulerStartX(int startX){
        this.rulerStartX = startX;
    }

    public void setupData() {
        ShiftHashData= new HashMap<>();
    }

    public void setupData(List<IShift> shifts) {
        ShiftHashData= new HashMap<>();

        if (shifts == null)
            return;

        for( IShift shift : shifts){
            if (!ShiftHashData.containsKey(shift.getSectionName())) {
                List<IShift> list = new ArrayList<>();
                list.add(shift);

                ShiftHashData.put(shift.getSectionName(), list);
            } else {
                ShiftHashData.get(shift.getSectionName()).add(shift);
            }
        }
    }

    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        int measuredHeight = measureHeight(hMeasureSpec);
        int measuredWidth = measureWidth(wMeasureSpec);

        setMeasuredDimension(measuredHeight, measuredWidth);
    }

    protected int measureHeight(int measureSpec) {
        return viewWidth;
    }

    protected int measureWidth(int measureSpec) {
        return VIEW_HEIGHT;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        ShiftRects = new ArrayList<>();
        drawTimeLabels(canvas);

        drawShifts(canvas);
    }

    int MaxHeight = 0;

    protected void drawShifts(Canvas canvas) {
        int px = 10;

        Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(DEPARTMENT_FONT_SIZE);
        mTextPaint.setColor(Color.DKGRAY);

        Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.LTGRAY);

        ArrayList<List<IShift>> departments = new ArrayList<>();
        departments.addAll(ShiftHashData.values());

        Collections.sort(departments, new Comparator<List<IShift>>() {
            @Override
            public int compare(List<IShift> lhs, List<IShift> rhs) {
                return lhs.get(0).getSectionName().compareTo(rhs.get(0).getSectionName());
            }
        });

        for (List<IShift> department : departments) {
            List<ShiftLevel> added = new ArrayList<>();
            MaxHeight = 0;


            for (IShift shift : department) {
                // To check if the shift collides with other in the department
                drawRoundRect(canvas, shift, department, added);
            }

            IShift firstElement = department.get(0);

            // Write Dep. Name
            canvas.drawText(firstElement.getSectionName(), px, py + DEPARTMENT_FONT_SIZE + DEPARTMENT_VERTICAL_PADDING, mTextPaint);
            py = ROUND_RECT_PADDING + MaxHeight;
            canvas.drawLine(0, py, viewWidth, py, mLinePaint);
        }
    }

    public void drawRoundRect(Canvas canvas, IShift shift, List<IShift> department, List<ShiftLevel> added) {
        boolean collides = false;
        for (IShift checkCollide : department) {
            if (!shift.equals(checkCollide) && collidesWithShift(shift,checkCollide)) {
                collides = true;
                break;
            }
        }

        Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int color = shift.getColor();
        mRectPaint.setColor(getContext().getResources().getColor(color != 0 ? color : R.color.colorPrimary));

        int ix = dateTimeUtility.getHour(shift.getStartDate()) - initialHour;
        int fx = dateTimeUtility.getHour(shift.getEndDate()) - initialHour;

        if (ix >= totalHours || fx >= totalHours)
            return;

        boolean hitTest;
        RectF tempRect = new RectF();
        int level = 1;

        do {
            hitTest = false;
            tempRect.top = py + ROUND_RECT_PADDING + (ROUND_RECT_PADDING * (level - 1)) + ((ROUND_RECT_HEIGHT / (collides ? 2 : 1)) * (level - 1));
            tempRect.bottom = py + ROUND_RECT_PADDING + (ROUND_RECT_PADDING * (level - 1)) + (ROUND_RECT_HEIGHT / (collides ? 2 : 1)) * level;
            tempRect.left = labelXPosition[ix];
            tempRect.right = labelXPosition[fx] - ROUND_HORIZONTAL_PADDING;

            if (collides) {
                for (ShiftLevel shiftLevel : added) {
                    if (collidesWithShift(shiftLevel.shift,shift) && shiftLevel.level == level) {
                        hitTest = true;
                        level += 1;
                        break;
                    }
                }
            }

        } while (hitTest);

        if (tempRect.bottom > MaxHeight)
            MaxHeight = (int) tempRect.bottom;

        added.add(new ShiftLevel(level, shift));
        ShiftRects.add(new ShiftRect(tempRect, shift));
        canvas.drawRoundRect(tempRect, ROUND_RECT_RADIUS, ROUND_RECT_RADIUS, mRectPaint);


        Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(ROUND_TEXT_SIZE);

        int textX = labelXPosition[ix] + ROUND_TEXT_HORIZONTAL_PADDING;
        int textY = py + (ROUND_RECT_PADDING * (level - 1)) + ((ROUND_RECT_HEIGHT / (collides ? 2 : 1)) * (level - 1)) + (ROUND_RECT_PADDING + ROUND_RECT_HEIGHT / (collides ? 4 : 2) - ROUND_TEXT_SIZE + 10);

        String time = String.format("%s - %s ",
                new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(shift.getStartDate()),
                new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(shift.getEndDate()));

        String text = shift.getName();

        if (fx - ix > 4 && timeVisibility == View.VISIBLE) {
            text = time + text;
        }

        canvas.drawText(text, textX, textY + ROUND_TEXT_SIZE, mTextPaint);
    }

    public void drawTimeLabels(Canvas canvas) {
        int px = rulerStartX;
        py = TIME_LABEL_FONT_SIZE + TIME_LABEL_VERTICAL_PADDING;

        Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(TIME_LABEL_FONT_SIZE);
        mTextPaint.setColor(Color.LTGRAY);

        for (int i = 0; i < totalHours; i++) {
            String displayText = String.format("%02d:00", initialHour + i);
            float textWidth = mTextPaint.measureText(displayText);
            canvas.drawText(displayText, px, py, mTextPaint);
            labelXPosition[i] = px;
            px += textWidth + 10;
        }

        viewWidth = px;

        py += TIME_LABEL_VERTICAL_PADDING;

        canvas.drawLine(0, py, viewWidth, py, mTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionPerformed = event.getAction();
        if (actionPerformed == MotionEvent.ACTION_UP)
            for (ShiftRect sr : ShiftRects) {
                if (sr.isPointOnRect(event.getX(), event.getY())) {
                    if (shiftClickListener != null){
                        shiftClickListener.onShiftClick(sr.shift);
                    }
                    break;
                }
            }

        return true;
    }

    public void setOnShiftClickListener(OnShiftClickListener onShiftClickListener) {
        this.shiftClickListener = onShiftClickListener;
    }

    public int getTimeVisibility() {
        return timeVisibility;
    }

    public void setTimeVisibility(int timeVisibility) {
        this.timeVisibility = timeVisibility;
    }

    public boolean collidesWithShift(IShift shift1, IShift shift2){
        if (dateTimeUtility.compareDatesByMinutePrecision(shift1.getStartDate(), shift2.getStartDate()) || dateTimeUtility.compareDatesByMinutePrecision(shift1.getEndDate(), shift2.getEndDate())){
            return true;
        }

        if (shift1.getStartDate().before(shift2.getEndDate()) && shift1.getEndDate().after(shift2.getStartDate())){
            return true;
        }

        if (shift2.getStartDate().before(shift1.getEndDate()) && shift2.getEndDate().after(shift1.getStartDate())){
            return true;
        }

        return false;
    }

    class ShiftLevel {
        public int level;
        public IShift shift;

        public ShiftLevel(int level, IShift shift) {
            this.level = level;
            this.shift = shift;
        }
    }

    class ShiftRect {
        public RectF rect;
        public IShift shift;

        public ShiftRect(RectF rect, IShift shift) {
            this.rect = rect;
            this.shift = shift;
        }

        public boolean isPointOnRect(float x, float y) {
            return (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom);
        }
    }

    public interface OnShiftClickListener {
        void onShiftClick(IShift shift);
    }

    public interface IShift {
        String getSectionName();
        Date getStartDate();
        Date getEndDate();
        String getName();
        int getColor();
    }
}
