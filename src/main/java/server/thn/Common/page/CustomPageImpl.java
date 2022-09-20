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
    //새로운 인스턴스 변수 생성


    private final long total;

    @Override
    public long getTotalElements() {
        return total;
    }

    @Override
    public int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
    }

    @Override
    public boolean hasNext() {
        return getNumber() + 1 < getTotalPages();
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    // 0811 : content 넘겨줄 때 이미 paging 이 레포지토리 단에서 완료된 채로 넘어와져야 한다. 레포지토리에서
    public CustomPageImpl(List<T> content, Pageable pageable, long total, List<String> indexes, @Nullable List<Long> allResponsibleList, long total1) {
        super(content, pageable, total);
        this.indexes = indexes;
        this.allResponsibleList = allResponsibleList;
        this.total = total1;
    }


    public CustomPageImpl(List<T> content, List<String> indexes, @Nullable List<Long> allResponsibleList, long total1) {
        super(content);
        this.indexes = indexes;
        this.allResponsibleList = allResponsibleList;
        this.total = total1;
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

