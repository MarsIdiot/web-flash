import request from '@/utils/request'

export default {
  getList: function (params) {
    return request({
      url: '/buy/list',
      method: 'get',
      params
    })
  },
  exportXls: function (params) {
    return request({
      url: '/buy/export',
      method: 'get',
      params
    })
  },
  transfer: function (params) {
    return request({
      url: '/buy/transfer',
      method: 'post',
      data: params
    })
  },
  add: function (params) {
    return request({
      url: '/buy',
      method: 'post',
      data: params
    })
  },
  update: function (params) {
    return request({
      url: '/buy',
      method: 'put',
      data: params
    })
  },
  remove: function (id) {
    return request({
      url: '/buy',
      method: 'delete',
      params: {
        id: id
      }
    })
  }
}
