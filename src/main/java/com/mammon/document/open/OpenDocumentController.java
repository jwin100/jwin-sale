package com.mammon.document.open;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.document.domain.query.DocumentHelpQuery;
import com.mammon.document.domain.vo.DocumentHelpPageVo;
import com.mammon.document.domain.vo.DocumentHelpVo;
import com.mammon.document.domain.vo.DocumentResourceTreeVo;
import com.mammon.document.service.DocumentHelpService;
import com.mammon.document.service.DocumentResourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @since 2024/8/2 14:25
 */
@RestController
@RequestMapping("/open/document")
public class OpenDocumentController {

    @Resource
    private DocumentHelpService documentHelpService;

    @Resource
    private DocumentResourceService documentResourceService;

    @GetMapping("/resource/tree")
    public ResultJson<List<DocumentResourceTreeVo>> getTree() {
        return ResultJson.ok(documentResourceService.getTree());
    }

    @GetMapping("/help/page")
    public ResultJson<PageVo<DocumentHelpPageVo>> getPage(DocumentHelpQuery query) {
        return ResultJson.ok(documentHelpService.getPage(query));
    }

    @GetMapping("/help/{id}")
    public ResultJson<DocumentHelpVo> getInfo(@PathVariable String id) {
        return ResultJson.ok(documentHelpService.getInfo(id));
    }
}
