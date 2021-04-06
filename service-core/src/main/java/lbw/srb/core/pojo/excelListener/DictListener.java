package lbw.srb.core.pojo.excelListener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lbw.srb.core.pojo.entity.Dict;
import lbw.srb.core.service.DictService;

import java.util.ArrayList;
import java.util.List;

public class DictListener extends AnalysisEventListener<Dict> {
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
//    private static final int BATCH_COUNT = 3000;
    List<Dict> list = new ArrayList<Dict>();
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private DictService dictService;


    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     */
    public DictListener(DictService dictService) {
        this.dictService = dictService;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     *            one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(Dict dict, AnalysisContext context) {
//        list.add(dict);
//        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
//        if (list.size() >= BATCH_COUNT) {
//            saveData();
//            // 存储完成清理 list
//            list.clear();
//        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
//        LOGGER.info("{}条数据，开始存储数据库！", list.size());
//        uploadDAO.save(list);
//        LOGGER.info("存储数据库成功！");
        dictService.saveBatch(list);
    }
}
