import os

def scan_project_files(project_path, output_file):
    # 打开输出文件
    with open(output_file, 'w', encoding='utf-8') as f:
        # 递归遍历项目目录
        for root, dirs, files in os.walk(project_path):
            for file in files:
                # 获取文件完整路径
                file_path = os.path.join(root, file)
                
                # 检查文件扩展名
                if file.endswith('.java') or file.endswith('.sql') or file == 'pom.xml':
                    try:
                        # 写入分隔线
                        f.write('\n' + '='*80 + '\n')
                        # 写入文件名
                        f.write(f'File Name: {file}\n')
                        # 写入文件路径
                        f.write(f'File Path: {file_path}\n')
                        f.write('\nFile Content:\n')
                        
                        # 读取并写入文件内容
                        with open(file_path, 'r', encoding='utf-8') as source_file:
                            content = source_file.read()
                            f.write(content)
                            
                    except Exception as e:
                        f.write(f'\nError reading file: {str(e)}\n')

if __name__ == '__main__':
    # 设置项目路径
    project_path = os.getcwd()
    # 设置输出文件路径
    output_file = 'projectCode.txt'
    
    # 检查项目路径是否存在
    if os.path.exists(project_path):
        scan_project_files(project_path, output_file)
        print(f"扫描完成! 结果已保存到 {output_file}")
    else:
        print("项目路径不存在!")