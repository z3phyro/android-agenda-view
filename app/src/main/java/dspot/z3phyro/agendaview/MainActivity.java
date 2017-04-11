package dspot.z3phyro.agendaview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Maykel on 4/6/2017.
 */

public class MainActivity extends AppCompatActivity {
    AgendaView agendaView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    void init() {
        agendaView = (AgendaView) findViewById(R.id.agView);

        List<AgendaView.IShift> shifts = new ArrayList<>();
        DateTimeUtility dt = DateTimeUtility.getInstance();

        for (int i = 0; i < 10; i++) {
            String depName = i % 2 == 0 ? "DSpot.me" : "Ciberos";
            String name = "Some name " + i;
            Date start = dt.setHourMinuteToDate(new Date(), i + 6, 0);
            Date end = dt.setHourMinuteToDate(new Date(), i + 6 + 5, 0);
            TestClass shift = new TestClass(depName, name, start, end);

            shifts.add(shift);
        }

//        agendaView.setRulerStartX(280);
        agendaView.setTimeVisibility(View.VISIBLE);
        agendaView.setInitialHour(4);
        agendaView.setTotalHours(20);
        agendaView.setOnShiftClickListener(new AgendaView.OnShiftClickListener() {
            @Override
            public void onShiftClick(AgendaView.IShift shift) {
                Toast.makeText(getBaseContext(), shift.getName(), Toast.LENGTH_LONG).show();
            }
        });
        agendaView.setupData(shifts);
        agendaView.invalidate();
    }

    class TestClass implements AgendaView.IShift {
        String DepartmentName;
        String Name;
        Date StartDate;
        Date EndDate;

        public TestClass(String DepartmentName, String Name, Date StartDate, Date EndDate) {
            this.DepartmentName = DepartmentName;
            this.Name = Name;
            this.StartDate = StartDate;
            this.EndDate = EndDate;
        }

        @Override
        public String getSectionName() {
            return DepartmentName;
        }

        @Override
        public Date getStartDate() {
            return StartDate;
        }

        @Override
        public Date getEndDate() {
            return EndDate;
        }

        @Override
        public String getName() {
            return Name;
        }

        @Override
        public int getColor() {
            return DepartmentName.charAt(0) == 'D' ? R.color.colorAccent : R.color.colorPrimary;
        }
    }
}
