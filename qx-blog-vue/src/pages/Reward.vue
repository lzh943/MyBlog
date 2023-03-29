
<template>
<div>
	<sg-nav></sg-nav>
	<div class="container">
    <sg-reward></sg-reward>
	</div>
</div>
</template>

<script>
import header from '../components/header.vue'
import reward from '../components/reward.vue'
export default {
	name: 'Reward',
	data() { //选项 / 数据
		return {

		}
	},
	methods: { //事件处理器

	},
	components: { //定义组件
		'sg-nav': header,
		'sg-reward': reward,
	},
	created() { //生命周期函数

	}
}
</script>

<style>

</style>
