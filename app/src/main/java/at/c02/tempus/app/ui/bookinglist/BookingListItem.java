package at.c02.tempus.app.ui.bookinglist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import at.c02.tempus.app.ui.utils.DateUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by Daniel Hartl on 13.04.2017.
 */

public class BookingListItem extends AbstractFlexibleItem<BookingListItem.BookingListItemViewHolder> {

    private Long id;
    private String projectName;
    private Date beginDate;
    private Date endDate;

    public BookingListItem(Long id, String projectName, Date beginDate, Date endDate) {
        this.id = id;
        this.projectName = projectName;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public String getProjectName() {
        return projectName;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookingListItem that = (BookingListItem) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public int getLayoutRes() {
        return android.R.layout.simple_list_item_2;
    }

    @Override
    public BookingListItemViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new BookingListItemViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, BookingListItemViewHolder holder, int position, List payloads) {
        DateFormat dateTimeFormat = DateUtils.getDateTimeFormat();
        holder.text1.setText((beginDate != null ? dateTimeFormat.format(beginDate) : "?")
                + " - "
                + (endDate != null ? dateTimeFormat.format(endDate) : "?")
        );
        holder.text2.setText(projectName);
    }

    public class BookingListItemViewHolder extends FlexibleViewHolder {

        @BindView(android.R.id.text1)
        public TextView text1;

        @BindView(android.R.id.text2)
        public TextView text2;

        public BookingListItemViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
