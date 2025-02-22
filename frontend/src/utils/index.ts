//将身份转化为中文显示
export function parseRole(role: number | null) {
    if (role === 1) {
        return "企业"
    } else if (role === 2) {
        return "金融机构"
    }
}

//将时间转化为日常方式
export function parseTime(time: string) {
    let times = time.split(/T|\./)
    return times[0] + " " + times[1]
}



