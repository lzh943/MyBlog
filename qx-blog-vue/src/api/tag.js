import request from '@/utils/request'


// 查询分类列表
export function listAllTag() {
  return request({
    url: '/tag/listAllTag',
    method: 'get'
  })
}
// 查询分类详细

