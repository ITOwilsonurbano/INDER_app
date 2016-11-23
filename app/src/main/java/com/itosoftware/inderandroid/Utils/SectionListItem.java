package com.itosoftware.inderandroid.Utils;

public class SectionListItem {
    public Integer id;
    public Object item;
    public String section;

    public SectionListItem(final Integer id, final Object item, final String section) {
        super();
        setId(id);
        setItem(item);
        setSection(section);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return item.toString();
    }

}