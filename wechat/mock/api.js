/**
 * Created by Neo on 2017/5/10.
 */
// 模拟延迟
Mock.setup({
    timeout: '2000'
});
/**
 * 使用正则超级(｡･∀･)ﾉﾞ嗨
 * resultCode 多个200，减少错误概率 ~囧~
 */
Mock.mock(/\/api\/teamList/, "get", {
    "resultCode": 200,
    "resultMsg": "ok",
    "data|10": [
        {
            name: "@cname",
            rank: '@pick(["总代理", "分代理", "经销商", "爱心天使"])',
            joinTime: '@now("yyyy-MM-dd")',
            phone: /^1(3|4|5|7|8)\d{9}$/
        }
    ]
});

Mock.mock(/\/api\/orderList/, "get", {
    "resultCode": 200,
    "resultMsg": "ok",
    "data|10": [
        {
            orderId: '@id',
            orderTime: '@now("yyyy-MM-dd")',
            orderStatus: '@pick(["成功", "失败", "取消"])',
            name: "@cname",
            goodsInfo: '饮水机 u56立式',
            orderAmount: '@integer(3000, 9999999)',
            package: '3年收费 730天',
            quantity: '@integer(1, 100)',
            phone: /^1(3|4|5|7|8)\d{9}$/
        }
    ]
});

/**
 * equipmentStatus 暂定Number
 * 0: 正常使用中
 * 1: 维护中
 * 2: 维修中
 * 3: 退款中
 */

Mock.mock(/\/api\/equipmentList/, "get", {
    "resultCode": 200,
    "resultMsg": "ok",
    "data|10": [
        {
            equipmentId: '@id',
            id: '@id',
            equipmentStatus: '@integer(0, 3)',
            remainingTime: '@integer(1, 1095)',
            TDS: "@integer(1, 600)",
            installationAddress: '@county(true)',
            installationTime: '@now("yyyy-MM-dd")'
        }
    ]
});