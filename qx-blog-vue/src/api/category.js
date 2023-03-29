import request from '@/utils/request'

// 查询分类列表
export function getCategoryList() {
    return request({
        url: '/category/getCategoryList',
        headers: {
          isToken: false
        },
        method: 'get'
    })
}
export function listAllCategory() {
  return request({
    url: '/category/listAllCategory',
    method: 'get'
  })
}
