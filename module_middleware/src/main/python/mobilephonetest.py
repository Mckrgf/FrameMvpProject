from java import jclass

origin_arr = []
countstop = []
data_arr = []
temp_arr = []
first = 1
full = 0
run = 0
TAG = "StepAnalysis"
def insertData(dataX, dataY, dataZ):
    origin_arr.append([float(dataX), float(dataY), float(dataZ)])
    # print("aaa", float(dataX), float(dataY), float(dataZ))
    if len(origin_arr) >= 50:
        getdata(origin_arr)  # 得到数据数组
        runm = caldata(data_arr)
        # print(TAG + "run:" + runm + JavaBean().PORT)
        origin_arr.clear()
        return runm

def getStatus():
    return run



def caldata(data_arr):
    count = 0
    acc_sd_sum = 0.0
    acc_square_sum = 0.0  # 平方和
    acc_aLL = 0.0
    acc_cv = 0.0
    acc = []
    index = 0
    stopm = 0
    runm = "静止"
    count_stops = 0

    for data in data_arr:
        acc_sum = data[0] ** 2 + data[1] ** 2 + data[2] ** 2  # 求三轴平方（合加速度平方）
        unit = acc_sum ** 0.5
        acc.append(unit)  # 合加速度
        acc_aLL = acc_aLL + unit  # 合加速度的和
        acc_square_sum += acc_sum
        if unit < 10.421 and unit > 9.18:  # 统计处于稳定区间内的加速度个数
            count += 1

    per = (count / len(data_arr)) * 100  # 计算合加速度大于1.1所占样本的百分比

    acc_average = acc_aLL / len(data_arr)  # 合加速度的均值
    for i in range(len(data_arr)):
        acc_sd_sum += (acc[index] - acc_average) ** 2
        index = index + 1

    acc_sd = (acc_sd_sum / len(data_arr)) ** 0.5  # 求标准差
    # acc_cv = acc_sd/acc_average

    if per > 67:
        stopm = 1
    else:
        if per <= 67 and per > 40:
            if acc_sd >= 1.1:
                stopm = 0
            else:
                stopm = 1
        else:
            stopm = 0

        countstop.insert(0, stopm)
        if len(countstop) >= 3:  # 数组长度大于3时可进入逻辑
            for i in range(2):
                if countstop[i] > 0:  # 累计计算stop=1时的次数
                    count_stops = count_stops + 1
                if len(countstop) > 3:  # 控制存储数组的长度
                    countstop.pop(3)
            if count_stops > 1:
                runm = "静止"  # 静止状态
                run = 0
            else:
                runm ="运动"  # 运动状态
                run = 1

    # print(TAG + "百分比是：", per)
    # print(TAG + "标准差是：", acc_sd)
    # print("累计状态次数：", count_stops)
    return runm


def getdata(origin_arr):
    size = len(origin_arr)
    for i in range(size):
        data_arr.insert(i, origin_arr[i])
        if len(data_arr) > 2 * size:
            data_arr.pop(2 * size - 1)
    # print(data_arr)