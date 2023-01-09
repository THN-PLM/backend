package server.thn.Common.page;


import com.fasterxml.jackson.annotation.JsonGetter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.List;

public class CustomPageImpl<T> extends PageImpl<T> {
    private final List<String> indexes;

    @Nullable
    private final List<Long> allResponsibleList;

    private final int requestPageSize;

    private final int requestPageNumber;

    private final long total;

    @Override
    public long getTotalElements() {
        return total;
    }

    @Override
    public int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getRequestPageSize());
    }

    private int getRequestPageSize() {
        return requestPageSize;
    }

    @Override
    public boolean hasNext() {
        return getNumber() + 1 < getTotalPages();
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    public CustomPageImpl(List<T> content, Pageable pageable, long total, List<String> indexes, @Nullable List<Long> allResponsibleList, long total1 , int requestPageSize, int requestPageNumber ) {
        super(content, pageable, total);
        this.indexes = indexes;
        this.allResponsibleList = allResponsibleList;
        this.total = total1;
        this.requestPageSize = requestPageSize;
        this.requestPageNumber = requestPageNumber;
    }


    public CustomPageImpl(List<T> content, List<String> indexes, @Nullable List<Long> allResponsibleList, long total1, int requestPageSize, int requestPageNumber ) {
        super(content);
        this.indexes = indexes;
        this.allResponsibleList = allResponsibleList;
        this.total = total1;
        this.requestPageSize = requestPageSize;
        this.requestPageNumber = requestPageNumber;
    }


    @JsonGetter(value = "contents")
    @Override
    public List getContent() {
        return super.getContent();
    }

    // 이 json getter 을 설정안해주면 json 반환에 아무것도 없음
    @JsonGetter(value = "indexes") //json 키 값에 보이게 되는 이름
    public List getPaging() {
        return indexes;
    }

    @JsonGetter(value = "all") //json 키 값에 보이게 되는 이름
    public List getIds() {
        return allResponsibleList;
    }

}
