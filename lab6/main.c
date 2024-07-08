#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct File {//文件
    char name[32];
    char *content;
    int size;

} File;

typedef struct Directory {//目录
    char name[32];
    File *files[32];
    int file_count;//目录数量
    struct Directory *subdirectories[32];//子目录数组
    int subdir_count;//子目录个数
    struct Directory *parent_directory;//根目录
} Directory;

Directory *root;// 文件系统根目录
Directory *current;// 当前目录

Directory *create_directory(const char *name) {//新建目录
    Directory *dir = (Directory *) malloc(sizeof(Directory));//分配内存
    strcpy(dir->name, name);
    dir->file_count = 0;
    dir->subdir_count = 0;
    dir->parent_directory = NULL;
    return dir;
}

File *create_file(const char *name, const char *content) {//新建文件
    File *file = (File *) malloc(sizeof(File));//分配内存
    strcpy(file->name, name);
    file->content = strdup(content);
    file->size = strlen(content);
    return file;
}

void add_file_to_directory(Directory *dir, File *file) {//在目录下添加文件
    dir->files[dir->file_count++] = file;
}

void add_subdirectory(Directory *parent, Directory *child) {//在目录下新建文件夹（目录）
    parent->subdirectories[parent->subdir_count++] = child;
    child->parent_directory = parent;
}

void list_directory(Directory *dir) {//目录列表-ls查看
    printf("Directory: %s\n", dir->name);
    int i;
    for (i = 0; i < dir->file_count; i++) {
        printf(" File: %s (%d bytes)\n", dir->files[i]->name, dir->files[i]->size);
    }
    for ( i = 0; i < dir->subdir_count; i++) {
        printf(" Subdirectory: %s\n", dir->subdirectories[i]->name);
    }
}

Directory *find_subdirectory(Directory *dir, const char *name) {//查找子目录
    int i;
    for ( i = 0; i < dir->subdir_count; i++) {
        if (strcmp(dir->subdirectories[i]->name, name) == 0) {
            return dir->subdirectories[i];
        }
    }
    return NULL;
}

File *find_file(Directory *dir, const char *name) {//查找文件
    int i;
    for ( i = 0; i < dir->file_count; i++) {
        if (strcmp(dir->files[i]->name, name) == 0) {
            return dir->files[i];
        }
    }
    return NULL;
}

void remove_file_from_directory(Directory *dir, const char *name) {//从目录中删除文件
    int i;
    for ( i = 0; i < dir->file_count; i++) {
        if (strcmp(dir->files[i]->name, name) == 0) {
            free(dir->files[i]->content);//释放文件内容
            free(dir->files[i]);//释放文件空间
            int j;
            for ( j = i; j < dir->file_count - 1; j++) {
                dir->files[j] = dir->files[j + 1];
            }
            dir->file_count--;
            break;
        }
    }
}

void remove_subdirectory(Directory *dir, const char *name) {//删除子目录
    int i;
    for ( i = 0; i < dir->subdir_count; i++) {
        if (strcmp(dir->subdirectories[i]->name, name) == 0) {
            free(dir->subdirectories[i]);
            int j;
            for ( j = i; j < dir->subdir_count - 1; j++) {
                dir->subdirectories[j] = dir->subdirectories[j + 1];
            }
            dir->subdir_count--;
            break;
        }
    }
}

void go_back_to_parent_directory() {//返回根目录
    if (current->parent_directory != NULL) {
        current = current->parent_directory;//当前文件返回其根目录
    }
}

int main() {
    root = create_directory("/");//创建目录
    current = root;
printf("Welcome to the custom shell! \n");
    char command[32], arg1[32], arg2[32];
    while (1) {
        printf("%s> ", current->name);//打印当前目录名字
        scanf("%s", command);//输入要求

        if (strcmp(command, "ls") == 0) {
            list_directory(current);//查看当前目录下的文件和文件夹信息命令。
        } else if (strcmp(command, "cd") == 0) {//进入下级目录命令
            scanf("%s", arg1);//输入下级目录名字
            if (strcmp(arg1, "..") == 0) {
                go_back_to_parent_directory();//cd ..进入根目录
            } else {
                Directory *next = find_subdirectory(current, arg1);//查找下一级目录
                if (next != NULL) {//存在
                    current = next;
                } else {//不存在-输出
                    printf("Directory not found.\n");
                }
            }
        } else if (strcmp(command, "mv") == 0) {//移动文件命令
            scanf("%s %s", arg1, arg2);//old文件内容，new文件
            File *file = find_file(current, arg1);//查找old文件
            if (file != NULL) {
                strcpy(file->name, arg2);
            } else {
                printf("File not found.\n");
            }
        } else if (strcmp(command, "touch") == 0) {//新建文件命令
            scanf("%s", arg1);//输入文件名
            File *file = create_file(arg1, "");//新建文件
            add_file_to_directory(current, file);
        } else if (strcmp(command, "mkdir") == 0) {//新建文件夹命令
            scanf("%s", arg1);//输入文件夹名
            Directory *dir = create_directory(arg1);//新建目录
            add_subdirectory(current, dir);//添加子目录
        } else if (strcmp(command, "rm") == 0) {//删除文件命令
            scanf("%s", arg1);//输入文件名
            remove_file_from_directory(current, arg1);
        } else if (strcmp(command, "rmdir") == 0) {//删除文件夹命令
            scanf("%s", arg1);//输入文件夹名
            remove_subdirectory(current, arg1);//子目录中删除文件夹
        } else if (strcmp(command, "read") == 0) {//从某文件内读取信息命令
            scanf("%s", arg1);
            File *file = find_file(current, arg1);
            if (file != NULL) {
                printf("%s\n", file->content);
            } else {
                printf("File not found.\n");
            }
        } else if (strcmp(command, "write") == 0) {//向某文件内写入信息命令
            scanf("%s", arg1);
            File *file = find_file(current, arg1);
            if (file != NULL) {
                char buffer[1024];
                scanf(" %[^\n]", buffer);//写入的信息
                file->content = strdup(buffer);
                file->size = strlen(buffer);
            } else {
                printf("File not found.\n");
            }
        } else if (strcmp(command, "exit") == 0) {//退出文件系统命令
            break;
        } else {
            printf("Unknown command: %s\n", command);
        }
    }

    return 0;
}
/*/> mkdir test
/> cd test
/test> touch example.txt
/test> write example.txt This is an example.
/test> ls
  File: example.txt (21 bytes)
/test> read example.txt
This is an example.
/test> mv example.txt new_example.txt
/test> ls
  File: new_example.txt (21 bytes)
/test> cd ..
/> ls
  Subdirectory: test
/> exit

*/
