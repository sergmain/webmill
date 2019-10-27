package org.riverock.gwt.client.commons;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.datepicker.client.CalendarUtil;

import java.util.Date;

@SuppressWarnings({"GWTStyleCheck", "unused"})
public class MonthDatePicker extends Composite implements HasValue<Date> {

	private PushButton backwards;
	private PushButton forwards;
	private Grid grid;
	private Date value;

	private static class DateChangeEvent extends ValueChangeEvent<Date> {

		public static <S extends HasValueChangeHandlers<Date> & HasHandlers> void fireIfNotEqualDates(
		      S source, Date oldValue, Date newValue) {
		    if (ValueChangeEvent.shouldFire(source, oldValue, newValue)) {
		      source.fireEvent(new DateChangeEvent(newValue));
		    }
		}

		protected DateChangeEvent(Date value) {
			super( CalendarUtil.copyDate(value) );
		}

		public Date getValue() {
			return CalendarUtil.copyDate( super.getValue() );
		}
	}

	public MonthDatePicker() {

		value = new Date();

		// Set up backwards.
		backwards = new PushButton();
        backwards.addHandler( handler -> {
                    Date newValue = CalendarUtil.copyDate( value );
                    CalendarUtil.addMonthsToDate( newValue, -1 );
                    setValue( newValue, true );
                },
                ClickEvent.getType()
        );

		backwards.getUpFace().setHTML("&laquo;");
		backwards.setStyleName("datePickerPreviousButton");

		forwards = new PushButton();
		forwards.getUpFace().setHTML("&raquo;");
		forwards.setStyleName("datePickerPreviousButton");
        forwards.addHandler( handler -> {
                    Date newValue = CalendarUtil.copyDate( value );
                    CalendarUtil.addMonthsToDate( newValue, +1 );
                    setValue( newValue, true );
                },
                ClickEvent.getType()
        );

	    // Set up grid.
	    grid = new Grid(1, 3);
	    grid.setWidget(0, 0, backwards);
	    grid.setWidget(0, 2, forwards);

	    final CellFormatter formatter = grid.getCellFormatter();
	    formatter.setStyleName(0, 1, "datePickerMonth");
	    formatter.setWidth(0, 0, "1");
	    formatter.setWidth(0, 1, "100%");
	    formatter.setWidth(0, 2, "1");
	    grid.setStyleName( "datePickerMonthSelector" );
	    initWidget(grid);
	    refresh();
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Date> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public final Date getValue() {
		  return CalendarUtil.copyDate( value );
	}

	public final void setValue(Date newValue) {
		  setValue( newValue, false );
	}

	public final void setValue(Date newValue, boolean fireEvents) {
		Date oldValue = CalendarUtil.copyDate( value );

		if (fireEvents && newValue != oldValue )
	        DateChangeEvent.fireIfNotEqualDates(this, oldValue, newValue);

		if ( newValue != oldValue ) {
			value = newValue; 
			refresh();
		}
	}

	public void refresh() {
		grid.setText( 0, 1, DateTimeFormat.getFormat("MMMM yyyy").format(value) );
	}
}
