namespace java projeto3sd 
namespace py projeto3sd



struct Page
{
    1:i64 creation,
    2:i64 modification,
    3:i32 version,
    4:binary data,
}


service FSService
{
    Page GetFile(1:string path),
    list<Page> ListFiles(1:string path),
    bool AddFile(1:string path, 2:binary data),
    Page UpdateFile(1:string path, 2:binary data),
    Page DeleteFile(1:string path),
    Page UpdateVersion(1:string path, 2:binary data, 3:i32 version),
    Page DeleteVersion(1:string path, 2:i32 version),
}
