# Android Agenda View

![Example image](https://github.com/z3phyro/android-agendaview/raw/master/example.png "Example image")

A very simple, interactive and extensible component to show the tasks/shifts schedule in a day/week. To use it you just need to add the following markup to you layout:

```
  <dspot.z3phyro.agendaview.AgendaView
      android:id="@+id/agView"
      android:layout_width="match_parent"
      android:layout_height="match_parent" />
```               
(beware of the package name for now :)

And then fill it with a class that implements the **AgendaView.IShift** interface:

```
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
```    

I uploaded the sample project so you can get anything you need there. On the code I made use of most of the configuration features.

```
  agendaView.setRulerStartX(280);
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
        
```

The control is the class **AgendaView.java** specifically but it depends on a **DateTimeUtility.java** class which I made to handle dates in Java. Also right now it's tightly coupled with a dimension values resource. I will work on that soon to make it a control that can be installed as a gradle dependency or else.

Hope it helps

 
